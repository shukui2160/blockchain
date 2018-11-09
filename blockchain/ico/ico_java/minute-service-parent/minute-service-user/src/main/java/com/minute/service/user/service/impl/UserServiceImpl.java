package com.minute.service.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.tookit.account.PasswdUtils;
import com.common.tookit.account.ValidateCodeUtils;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.user.common.UserEnum;
import com.minute.service.user.entry.User;
import com.minute.service.user.manage.EmailManage;
import com.minute.service.user.mapper.UserMapper;
import com.minute.service.user.service.UserService;

import cn.hutool.core.util.StrUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private EmailManage emailManage;

	@Autowired
	private UserMapper userMapper;

	@Override
	public TlabsResult sendEmail(String operation, String email, String languageId) {
		if ("regist".equals(operation)) {
			return sendEmail4Regist(email, languageId);
		} else if ("forget".equals(operation)) {
			return sendEmail4forgetPasswd(email, languageId);
		} else {
			throw new RuntimeException("operation parameter is error!");
		}
	}

	private TlabsResult sendEmail4Regist(String email, String languageId) {
		User user = userMapper.selectByEmail(email);
		if (user != null) {
			return ResultUtils.createResult(UserEnum.HAVE_REGIST.getCode(), UserEnum.HAVE_REGIST.getMsg(languageId));
		}
		String key = "regist#" + email;
		String code = ValidateCodeUtils.getCode();
		emailManage.sendEmail4Regist(email, code);
		redisTemplate.opsForValue().set(key, code, 60 * 30, TimeUnit.SECONDS);
		return ResultUtils.createSuccess();
	}

	private TlabsResult sendEmail4forgetPasswd(String email, String languageId) {
		User user = userMapper.selectByEmail(email);
		if (user == null) {
			return ResultUtils.createResult(UserEnum.ACCOUNT_ERROR.getCode(),
					UserEnum.ACCOUNT_ERROR.getMsg(languageId));
		}
		String key = "forget#" + email;
		String code = ValidateCodeUtils.getCode();
		emailManage.sendEmail4forgetPasswd(email, code);
		redisTemplate.opsForValue().set(key, code, 60 * 30, TimeUnit.SECONDS);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult verifyEmailCode(String operation, String email, String code, String languageId) {
		if ("regist".equals(operation)) {
			return verifyEmailCode4Regist(email, code, languageId);
		} else if ("forget".equals(operation)) {
			return verifyEmailCode4forgetPasswd(email, code, languageId);
		} else {
			throw new RuntimeException("operation parameter is error!");
		}
	}

	private TlabsResult verifyEmailCode4Regist(String email, String code, String languageId) {
		String key = "regist#" + email;
		String value = redisTemplate.opsForValue().get(key);
		if (StrUtil.isEmpty(value)) {
			// 验证码已过期
			return ResultUtils.createResult(UserEnum.EMAIL_CODE_EXPIRE.getCode(),
					UserEnum.EMAIL_CODE_EXPIRE.getMsg(languageId));
		}
		if (code.equals(value)) {
			// 防止 临界值问题
			redisTemplate.expire(key, 60 * 30, TimeUnit.SECONDS);
			return ResultUtils.createSuccess();
		} else {
			return ResultUtils.createResult(UserEnum.EMAIL_CODE_ERROR.getCode(),
					UserEnum.EMAIL_CODE_ERROR.getMsg(languageId));
		}
	}

	private TlabsResult verifyEmailCode4forgetPasswd(String email, String code, String languageId) {
		String key = "forget#" + email;
		String value = redisTemplate.opsForValue().get(key);
		if (StrUtil.isEmpty(value)) {
			// 验证码已过期
			return ResultUtils.createResult(UserEnum.EMAIL_CODE_EXPIRE.getCode(),
					UserEnum.EMAIL_CODE_EXPIRE.getMsg(languageId));
		}
		if (code.equals(value)) {
			// 防止 临界值问题
			redisTemplate.expire(key, 60 * 30, TimeUnit.SECONDS);
			return ResultUtils.createSuccess();
		} else {
			return ResultUtils.createResult(UserEnum.EMAIL_CODE_ERROR.getCode(),
					UserEnum.EMAIL_CODE_ERROR.getMsg(languageId));
		}
	}

	@Override
	public TlabsResult regist(String email, String code, String passwd, String markedWord, String languageId) {
		String key = "regist#" + email;
		String value = redisTemplate.opsForValue().get(key);
		if (StrUtil.isBlank(value)) {
			// return session失效！
			return ResultUtils.createResult(UserEnum.SESSION_EXPIRE.getCode(),
					UserEnum.SESSION_EXPIRE.getMsg(languageId));
		}
		if (!code.equals(value)) {
			// 移动端不用关注,此接口中的code 值依赖于上一个接口的 输入值,不是人手动输入
			// 注意看产品原形
			return ResultUtils.createResult(UserEnum.EMAIL_CODE_ERROR.getCode(),
					UserEnum.EMAIL_CODE_ERROR.getMsg(languageId));
		}
		// 数据库写入数据,创建用户信息
		User user = new User();
		user.setEmail(email);
		user.setPasswd(PasswdUtils.buildPasswd(passwd));
		user.setMarkedWord(markedWord);
		int rows = userMapper.insertSelective(user);
		if (rows == 0) {
			return ResultUtils.createResult(UserEnum.HAVE_REGIST.getCode(), UserEnum.HAVE_REGIST.getMsg(languageId));
		}
		// 初始化钱包信息

		// 删除邮箱验证码标示位
		redisTemplate.delete(key);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult login(String email, String passwd, String languageId) {
		// 判断账户是否已被锁定
		String key = "login#" + email;
		if (!redisTemplate.hasKey(key)) {
			redisTemplate.opsForValue().set(key, "0", 60, TimeUnit.MINUTES);
		}
		Long value = redisTemplate.opsForValue().increment(key, 1L);
		if (value >= 10) {
			redisTemplate.expire(key, 60, TimeUnit.MINUTES);
			return ResultUtils.createResult(UserEnum.USER_LOCKED.getCode(), UserEnum.USER_LOCKED.getMsg(languageId));
		}
		User user = userMapper.selectByEmail(email);
		if (user == null) {
			return ResultUtils.createResult(UserEnum.ACCOUNT_ERROR.getCode(),
					UserEnum.ACCOUNT_ERROR.getMsg(languageId));
		}
		if (!PasswdUtils.buildPasswd(passwd).equals(user.getPasswd())) {
			// return 密码错误！
			return ResultUtils.createResult(UserEnum.PASSWD_ERROR.getCode(), UserEnum.PASSWD_ERROR.getMsg(languageId));
		}
		// 登录成功后去除限制
		redisTemplate.delete(key);
		Map<String, Object> data = new HashMap<>();
		data.put("userId", user.getId());
		// data.put("createTime", System.currentTimeMillis());
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult modifyPasswd(String userId, String newpwd, String oldpwd, String languageId) {
		User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
		if (!PasswdUtils.buildPasswd(oldpwd).equals(user.getPasswd())) {
			// return 输入密码错误
			return ResultUtils.createResult(UserEnum.PASSWD_ERROR.getCode(), UserEnum.PASSWD_ERROR.getMsg(languageId));
		}
		user.setPasswd(PasswdUtils.buildPasswd(newpwd));
		user.setUpdateTime(new Date());
		userMapper.updateByPrimaryKey(user);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult forgetPasswd(String email, String code, String passwd, String languageId) {
		String key = "forget#" + email;
		String value = redisTemplate.opsForValue().get(key);
		if (value == null) {
			// return email code is 失效的 !
			return ResultUtils.createResult(UserEnum.EMAIL_CODE_EXPIRE.getCode(),
					UserEnum.EMAIL_CODE_EXPIRE.getMsg(languageId));
		}
		if (!code.equals(value)) {
			// return email code is 错误的！
			return ResultUtils.createResult(UserEnum.EMAIL_CODE_ERROR.getCode(),
					UserEnum.EMAIL_CODE_ERROR.getMsg(languageId));
		}
		User user = userMapper.selectByEmail(email);
		user.setPasswd(PasswdUtils.buildPasswd(passwd));
		user.setUpdateTime(new Date());
		userMapper.updateByPrimaryKey(user);
		redisTemplate.delete(key);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult existPayPasswd(String userId) {
		User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
		if (StrUtil.isNotEmpty(user.getPayPasswd())) {
			return ResultUtils.createSuccess(true);
		} else {
			return ResultUtils.createSuccess(false);
		}

	}

	@Override
	public TlabsResult initsPayPasswd(String userId, String payPasswd, String markedWord) {
		User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
		if (StrUtil.isEmpty(user.getPayPasswd())) {
			user.setPayPasswd(PasswdUtils.buildPasswd(payPasswd));
			user.setMarkedWord(markedWord);
			user.setUpdateTime(new Date());
			userMapper.updateByPrimaryKeySelective(user);
		} else {
			throw new RuntimeException("pay passwd is exit !");
		}
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult authPayPasswd(String userId, String payPasswd, String languageId) {
		User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
		if (!PasswdUtils.buildPasswd(payPasswd).equals(user.getPayPasswd())) {
			return ResultUtils.createResult(UserEnum.PASSWD_ERROR.getCode(), UserEnum.PASSWD_ERROR.getMsg(languageId));
		}
		return ResultUtils.createSuccess();
	}
	
	public static void main(String[] args) {
		System.out.println(PasswdUtils.buildPasswd("e10adc3949ba59abbe56e057f20f883e"));
	}

	@Override
	public TlabsResult selectEmaliById(Long userId) {
		// TODO Auto-generated method stub
		return ResultUtils.createSuccess(this.userMapper.selectEmailById(userId));
	}

}
