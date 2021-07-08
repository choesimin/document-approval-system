package com.simin.approval.controller;

import com.simin.approval.model.common.JwtManager;
import com.simin.approval.model.common.Pager;
import com.simin.approval.model.common.Status;
import com.simin.approval.model.dto.Approver;
import com.simin.approval.model.dto.Document;
import com.simin.approval.model.dto.ResponseData;
import com.simin.approval.model.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private JwtManager jwtManager;

    @GetMapping("/list")
    public ResponseData list(@RequestHeader String token, Document document, int current_page, boolean is_searching) {
        int user_seq = jwtManager.getSeqFromToken(token);
        document.setUser_seq(user_seq);

        List<Document> document_list;
        if (is_searching) {    // search mode
            document_list = documentService.listSearch(document);
        } else {    // normal mode
            document_list = documentService.list(document);
        }

        Pager pager = new Pager();
        List<Document> document_list_page = new ArrayList<Document>();    // list for paging

        pager.init(current_page, document_list.size(), 12);
        int current_position = pager.getCurrent_position();

        for (int i = 0; i < pager.getPage_size(); i++) {
            if (current_position >= pager.getTotal_record()) break;
            document_list_page.add(document_list.get(current_position));
            current_position = current_position + 1;
        }

        ResponseData responseData = new ResponseData();
        responseData.setData1(document_list_page);
        responseData.setData2(pager);
        responseData.setStatus(Status.SUCCESS);

        return responseData;
    }

    @GetMapping("/detail")
    public ResponseData detail(@RequestHeader String token, int seq) {
        int user_seq = jwtManager.getSeqFromToken(token);
        Document document_result = documentService.get(seq);

        int status = 0;    // 이 status는 현재 user에게 해당 문서의 결재 권한을 주는지에 대한 여부를 나타냄
        List<Approver> approver_list = document_result.getApprover_list();

        for (int i = 0; i < approver_list.size(); i++) {
            Approver approver = approver_list.get(i);

            boolean user_check = (user_seq == approver.getUser_seq());    // 현재 user가 해당 문서의 approver인지
            boolean turn_check = (document_result.getTurn() == approver.getTurn());    // 현재 user가 해당 문서의 결재 순서에 도달했는지
            boolean reject_check = !document_result.getState().equals("reject");    // 현재 문서가 reject된 상태인지

            if (user_check && turn_check && reject_check) {
                status = Status.SUCCESS;
            }
        }

        ResponseData responseData = new ResponseData();
        responseData.setData1(document_result);
        responseData.setStatus(status);

        return responseData;
    }

    @PostMapping("/regist")
    public ResponseData regist(@RequestHeader String token, @RequestBody Document document) {
        int user_seq = jwtManager.getSeqFromToken(token);
        document.setUser_seq(user_seq);
        documentService.regist(document);

        ResponseData responseData = new ResponseData();
        responseData.setStatus(Status.SUCCESS);

        return responseData;
    }

    @PostMapping("/approve")
    public ResponseData approve(Approver approver) {
        documentService.approve(approver);

        ResponseData responseData = new ResponseData();
        responseData.setStatus(Status.SUCCESS);

        return responseData;
    }
}