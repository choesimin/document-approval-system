package com.simin.approval.model.mapper;

import com.simin.approval.model.dto.Document;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocumentMapper {
	Document select(int seq);
	List<Document> selectOutbox(int user_seq);
	List<Document> selectInbox(int user_seq);
	List<Document> selectArchive(int user_seq);
	List<Document> selectOutboxSearch(Document document);
	List<Document> selectInboxSearch(Document document);
	List<Document> selectArchiveSearch(Document document);
	void insertDocument(Document document);
	void updateState(Document document);
	void updateFindate(Document document);
	void updateTurn(Document document);
}
