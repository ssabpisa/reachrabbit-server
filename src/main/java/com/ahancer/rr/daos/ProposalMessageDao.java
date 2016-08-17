package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.ProposalMessage;

public interface ProposalMessageDao extends CrudRepository<ProposalMessage, Long> {
	
	public Page<ProposalMessage> findByProposalProposalId(Long proposalId,Pageable pageable);
	
}
