package net.sinodata.business.entity;

public class PageVo {

	private Long start;
	private Long end;

	public PageVo(Integer pageNo, Long pageSize) {

		if (pageNo != null && pageSize != null) {
			if (pageNo < 1) {
				pageNo = 1;
			}
			start = (pageNo - 1) * pageSize;
			end = (pageNo) * pageSize;
		}

	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

}
