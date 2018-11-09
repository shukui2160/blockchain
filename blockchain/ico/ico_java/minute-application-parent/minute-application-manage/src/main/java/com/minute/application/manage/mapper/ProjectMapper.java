package com.minute.application.manage.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.application.manage.entry.Project;
@Mapper
public interface ProjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);
    
  //根据项目名称查询项目
  	List<Project> selectProjectByStatus(@Param("status") String status,@Param("project_name")String project_name);
  	
  	//修改项目状态
  	int updateProjectStatus(@Param("status")String status,@Param("id")Long id);
  	
  	//项目首页 列表
  	List<Project> indexList(Date currentTime);
  	
  	//项目首页 列表(根据用户id)
  	List<Project> indexListByUserId(@Param("userId") Integer userId,@Param("currentTime") Date currentTime);

}