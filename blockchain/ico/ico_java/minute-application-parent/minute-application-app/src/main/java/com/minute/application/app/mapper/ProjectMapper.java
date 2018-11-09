package com.minute.application.app.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.application.app.entry.Project;

@Mapper
public interface ProjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);
    
    // 项目首页 列表
 	List<Project> indexList(Date currentTime);

 	// 项目首页 列表(根据用户id)
 	List<Project> indexListByUserId(@Param("userId") Integer userId, @Param("currentTime") Date currentTime);
 	
 	// 查询我创建的项目
 	List<Project> createProjectByUserId(@Param("userId") Integer userId);
 	
 	//查询项目信息根据id
 	Project selectProjectMangeById(@Param("id") Integer id);
 	
 	//确认上线后修改项目状态值
 	int updateStatusById(@Param("status")String status,@Param("id") Long id);
 	
 	int makeSure(@Param("status")String status,@Param("id") Long id,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
 	
 	
}