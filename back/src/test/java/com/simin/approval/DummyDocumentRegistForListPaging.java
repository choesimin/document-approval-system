package com.simin.approval;

import com.simin.approval.model.dto.Approver;
import com.simin.approval.model.dto.Document;
import com.simin.approval.model.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DummyDocumentRegistForListPaging {

	@Autowired
	private DocumentService documentService;

	@Test
	void contextLoads() {
	    Document document = new Document();
		Approver approver = new Approver();
		List<Approver> approver_list = new ArrayList<Approver>();

		document.setUser_seq(1);
		approver.setUser_seq(1);
		approver_list.add(approver);
	    document.setApprover_list(approver_list);

	    for (int i = 0; i < 120; i++) {
			document.setTitle("dummy title for search" + (i + 1));
			document.setContent("dummy content" + (i + 1));
			documentService.regist(document);
		}
	}

}
