package com.simin.approval.model.common;

import lombok.Data;

@Data
public class Pager {
    private int current_page = 1;    // 현재 page
    private int total_record;    // list의 총 갯수
    private int page_size;    // 한 page에 담을 갯수
    private int total_page;    // 총 page 수
    private int block_size = 10;    // 한 번에 보여줄 page 이동 link의 갯수
    private int first_page;    // 현재 page에서의 첫번째 page 이동 link
    private int last_page;    // 현재 page에서의 마지막 page 이동 link
    private int current_position;    // 현재 page에서 가리키는 첫번째 record

    public void init(int current_page, int total_record, int page_size) {
        if (current_page > 0) {
            this.current_page = current_page;
        }
        this.total_record = total_record;
        this.page_size = page_size;
        this.total_page = (int)Math.ceil((float)total_record / page_size);
        this.first_page = current_page - (current_page - 1) % block_size;
        this.last_page = first_page + block_size - 1;
        this.current_position = (current_page - 1) * page_size;
    }
}
