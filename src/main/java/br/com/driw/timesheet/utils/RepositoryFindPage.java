package br.com.driw.timesheet.utils;

import org.springframework.data.domain.Page;

public interface RepositoryFindPage<T> {
	Page<T> findByPage(int page);
}
