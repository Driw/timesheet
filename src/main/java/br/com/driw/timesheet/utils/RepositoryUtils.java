package br.com.driw.timesheet.utils;

import br.com.driw.timesheet.Constants;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class RepositoryUtils {

	private RepositoryUtils() { }

	public static <T> List<T> findAllContentPages(Page<T> firstPage, RepositoryFindPage<T> findPage) {
		int totalElements = (int) firstPage.getTotalElements();

		List<T> items = new ArrayList<>(totalElements);
		int maxPages = (int) Math.ceil(totalElements / (float) Constants.MAX_PAGE_LENGTH);

		for (int pageCount = 0; pageCount < maxPages; pageCount++) {
			Page<T> page = findPage.findByPage(pageCount);
			items.addAll(page.getContent());
		}

		return items;
	}
}
