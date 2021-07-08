package com.simin.approval.model.service;

import com.simin.approval.exception.UnsuitableDataException;
import com.simin.approval.model.dto.Approver;
import com.simin.approval.model.dto.Document;
import com.simin.approval.model.mapper.ApproverMapper;
import com.simin.approval.model.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DocumentService {
	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private ApproverMapper approverMapper;

	public Document get(int seq) {
		Document document = documentMapper.select(seq);

		List<Approver> approver_list = approverMapper.selectByDocumentSeq(seq);
		document.setApprover_list(approver_list);

		return document;
	}

	public List<Document> list(Document document) {
		List<Document> document_list = new ArrayList<Document>();

		switch (document.getCategory()) {
			case "outbox":
				document_list = documentMapper.selectOutbox(document.getUser_seq());
				break;
			case "inbox":
				document_list = documentMapper.selectInbox(document.getUser_seq());
				break;
			case "archive":
				document_list = documentMapper.selectArchive(document.getUser_seq());
				break;
		}

		return document_list;
	}

	public List<Document> listSearch(Document document) {
	    List<Document> document_list = new ArrayList<Document>();

		switch (document.getCategory()) {
			case "outbox":
				document_list = documentMapper.selectOutboxSearch(document);
				break;
			case "inbox":
				document_list = documentMapper.selectInboxSearch(document);
				break;
			case "archive":
				document_list = documentMapper.selectArchiveSearch(document);
				break;
		}

		return document_list;
	}

	public void regist(Document document) {

	    checkDocumentRegistValidaion(document);
		documentMapper.insertDocument(document);

		List<Approver> approver_list = document.getApprover_list();

		checkApproverRegistValidation(approver_list);
		for (int i = 0; i < approver_list.size(); i++) {
			Approver approver = approver_list.get(i);
			approver.setDocument_seq(document.getSeq());
			approver.setTurn(i + 1);

			approverMapper.insert(approver);
		}
	}

	public void approve(Approver approver) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date time = new Date();
	    String now = format.format(time);

	    approver.setFindate(now);

        approverMapper.updateState(approver);
		approverMapper.updateOpinion(approver);
		approverMapper.updateFindate(approver);


		Document document = documentMapper.select(approver.getDocument_seq());
		document.setState(approver.getState());
		document.setFindate(approver.getFindate());

		int approver_count = approverMapper.countByDocumentSeq(document.getSeq());

		boolean is_approve = approver.getState().equals("approve");
		boolean is_reject = approver.getState().equals("reject");
		boolean is_turn = (document.getTurn() == approver_count);

		if (is_approve && is_turn) {
			documentMapper.updateState(document);
			documentMapper.updateFindate(document);
		} else if (is_reject) {
			documentMapper.updateState(document);
			documentMapper.updateFindate(document);

			// reject 시, 후순위 approver의 state를 cancel로 update
			List<Approver> approver_list = approverMapper.selectByDocumentSeq(approver.getDocument_seq());
			for (Approver approver_related : approver_list) {
				if (approver_related.getTurn() > document.getTurn()) {
					approver_related.setState("cancel");
					approverMapper.updateState(approver_related);
				}
			}
		}

		document.setTurn(document.getTurn() + 1);
		documentMapper.updateTurn(document);
	}

	private void checkDocumentRegistValidaion(Document document) {
		if (document.getTitle().equals("")) {
			throw new UnsuitableDataException("title is empty");
		}
		if (document.getContent().equals("")) {
			throw new UnsuitableDataException("content is empty");
		}
		if (document.getApprover_list().size() < 1) {
			throw new UnsuitableDataException("approver is empty");
		}
	}

	private void checkApproverRegistValidation(List<Approver> approver_list) {
		for (int i = 0; i < approver_list.size(); i++) {
			Approver approver = approver_list.get(i);

			int count = 0;
			for (int j = 0; j < approver_list.size(); j++) {
				if (approver.getUser_seq() == approver_list.get(j).getUser_seq()) {
					count = count + 1;
				}
			}
			if (count > 1) {
				throw new UnsuitableDataException("can not insert same approver");
			}
		}
	}
}
