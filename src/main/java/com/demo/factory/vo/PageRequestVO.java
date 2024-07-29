package com.demo.factory.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class PageRequestVO {
	@Getter @Setter
	private Long factoryNo;

	@Getter @Setter
	private Long userNo;

	@Getter @Setter
	private Long dashboardNo;

	@Getter @Setter
	private Long facilityNo;

	@Getter @Setter
	private Long facilityDataNo;

	private int page;
	private int size;
	
	private String searchType;
	private String keyword;

	public PageRequestVO() {
		this.page = 1;
		this.size = 10;
	}

	public void setPage(int page) {
		if (page <= 0) {
			this.page = 1;
			return;
		}

		this.page = page;
	}

	public void setSize(int size) {
		if (size <= 0 || size > 100) {
			this.size = 10;
			return;
		}

		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public int getPageStart() {
		return (this.page - 1) * size;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String toUriStringByPage(int page) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
			.queryParam("page", page)
			.queryParam("size", this.size)
			.queryParam("searchType", this.searchType)
			.queryParam("keyword", this.keyword)
			.build();
	
		return uriComponents.toUriString();
	}
	
	public String toUriString() {
		return toUriStringByPage(this.page);
	}	
	
	public String toUriStringForSearchAction(int page) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
			.queryParam("page", page)
			.build();
	
		return uriComponents.toUriString();
	}		
}
