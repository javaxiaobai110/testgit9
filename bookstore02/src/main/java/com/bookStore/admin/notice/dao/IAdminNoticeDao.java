package com.bookStore.admin.notice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bookStore.commons.beans.Notice;
import com.bookStore.utils.PageModel;

public interface IAdminNoticeDao {

	List<Notice> selectListNotice(PageModel pageModel);

	int insertNotice(Notice notice);

	Notice selectNoticeById(@Param("id")Integer id);

	int updateNotice(Notice notice);

	int deleteNoticeById(@Param("id")Integer id);

	int selectNoticeCount();

}
