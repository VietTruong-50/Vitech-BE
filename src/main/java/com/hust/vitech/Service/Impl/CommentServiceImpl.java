package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Comment;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Repository.CommentRepository;
import com.hust.vitech.Repository.CustomerRepository;
import com.hust.vitech.Repository.ProductRepository;
import com.hust.vitech.Request.CommentRequest;
import com.hust.vitech.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Comment createComment(CommentRequest commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Customer> customer = customerRepository.findCustomerByUserName(authentication.getName());

        if(customer.isPresent()){
            Comment comment = new Comment();

            comment.setContent(commentRequest.getContent());
            comment.setDateCreate(LocalDate.now());
            comment.setCustomer(customer.get());
            comment.setProduct(productRepository.findById(commentRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found")));

            return commentRepository.save(comment);
        }
        return null;
    }

    @Override
    public Comment deleteComment(Long commentId) {
        var comment = this.findByCommentId(commentId);

        commentRepository.deleteById(comment.getId());

        return comment;
    }

    @Override
    public Comment findByCommentId(Long id) {
        var entity = this.commentRepository.findById(id);
        if (entity.isEmpty()) {
            throw new ResourceNotFoundException("Comment not exist");
        }
        return entity.get();
    }

    @Override
    public Page<Comment> getCommentPagination(Long productId, int page, int size, String sortBy, String orderBy) {
//        if (!Objects.equals(orderBy, "ASC") && !Objects.equals(orderBy, "DSC")) {
//
//        }
        var product = productRepository.findById(productId);

        if(product.isPresent()){
            switch (orderBy) {
                case "ASC" -> product.get().getComments().sort(Comparator.comparing(Comment::getDateCreate));
                case "DSC" -> product.get().getComments().sort(Comparator.comparing(Comment::getDateCreate).reversed());
                default -> {
                }
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

            return new PageImpl<>(product.get().getComments(), pageable, product.get().getComments().size());
        }
       return null;
    }
}
