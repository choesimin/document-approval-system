package com.simin.approval.model.mapper;

import com.simin.approval.model.dto.Approver;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApproverMapper {
	void insert(Approver approver);
	List<Approver> selectByDocumentSeq(int document_seq);
	int countByDocumentSeq(int document_seq);
	void updateState(Approver approver);
	void updateOpinion(Approver approver);
	void updateFindate(Approver approver);
}
