//package com.ksh.myapp.post;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//import static com.ksh.myapp.post.QPost.post;
//@Repository
//public class PostRepositorySupport {
//
//    private JPAQueryFactory query;
//
//    @Autowired
//    public  PostRepositorySupport(EntityManager em) {
//
//        this.query = new JPAQueryFactory(em);
//    }
//
//    public Page<Post> searchPaging(String keyword, PageRequest pageReq) {
//        // SELECT
//        List<Post> content = query.
//                selectFrom(post)
//                .where(post.title.contains(keyword)
//                        .or(post.content.contains(keyword))
//                        .or(post.menu.contains(keyword))
//                        .or(post.dish.contains(keyword))
//                        .or(post.address.contains(keyword)))
//                .orderBy(post.no.desc())
//                .limit(pageReq.getPageSize()).offset(pageReq.getOffset())
//                .fetch();
//        // COUNT
//        Long total = query.select(post.count()).from(post)
//                .where(post.title.contains(keyword)
//                        .or(post.content.contains(keyword))).fetchFirst();
//
//        Page<Post> p = new PageImpl<>(content, pageReq, total);
//        return p;
//    }
//}
