package com.hasom.util;
/*
 * 페이지 나눔 기능에 사용할 정보를 기억하고 계산해 줄 클래스
 */
public class PageUtil {
	// 필요한 정보
	// ☆반드시 알려주어야 할 두 변수
	private int nowPage;
	private int totalCount;		// 총 데이터 개수
	
	// 개발자가 선택하는 변수
	private int pageList;	// 한페이지에 보여줄 목록의 개수
	private int groupCount;	// 한 화면에 나타날 페이지 수
	
	// 계산결과로 알아낼 변수
	private int totalPage;
	private int startPage;	// 페이지 나눔 기능에서 시작 페이지
	private int endPage;	// 페이지 나눔 기능에서 마지막 페이지
	
	
	// 생성자 함수를 이용해서 필수 데이터, 선택 데이터를 알아낸다.
	public PageUtil(int nowPage, int totalCount) {
		this(nowPage, totalCount, 5, 5);
	}
	
	public PageUtil(int nowPage, int totalCount, int pageList, int groupCount) {
		this.nowPage = nowPage;
		this.totalCount = totalCount;
		this.pageList = pageList;
		this.groupCount = groupCount;
		
		//나머지 정보를 계산하자;
		this.calcTotalPage();
		this.calcStartPage();
		this.calcEndPage();
		
//		System.out.println(" ");
//		System.out.println(">> pageList	: " + pageList);
//		System.out.println(">> groupCount	: " + groupCount);
//		System.out.println(">> 총 글 수		: " + totalCount);
//		System.out.println(">> 총 페이지	: " + totalPage);
//		System.out.println(">> 시작 페이지	: " + startPage);
//		System.out.println(">> 마지막페이지	: " + endPage);
		
	}
	
	// 계산하자
	public void calcTotalPage() {
		totalPage = totalCount / pageList;
		if ( totalCount % pageList != 0 ) {
			totalPage = totalPage + 1;
		}
	}
	public void calcStartPage() {
		// 이번에는 내가 보고 싶은 페이지를 가운데 오도록, 페이지 나눔 기능을 만들어보자.
		//	ex) 4 5 [6] 7 8  
		// 원리) 지금 보고 있는 페이지 앞쪽에 한 화면에 나타날 페이지 수의 절반이 놓여야 한다.
		startPage = nowPage - (groupCount / 2);
		// 		그런데, 그 시작은 최소한 1페이지 이후여야 한다.
		if( startPage < 1 ) {
			startPage = 1;
		}
	}
	public void calcEndPage() {
		// 원리) 시작 페이지 + 보여줄 페이지 개수 - 1
		endPage = startPage + groupCount - 1;
		//		그런데, 그 마지막은 마지막 페이지 보다 작아야 한다.
		if( endPage > totalPage ) {
			endPage = totalPage;
		}
	}

	
	// 이 정보는 뷰에서도 사용해야 하는 정보 이므로, 데이터 빈 클래스의 역활도 해야 한다.
	//	==> 게터와 세터가 필요하다.
	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageList() {
		return pageList;
	}

	public void setPageList(int pageList) {
		this.pageList = pageList;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	
}// 클래스
