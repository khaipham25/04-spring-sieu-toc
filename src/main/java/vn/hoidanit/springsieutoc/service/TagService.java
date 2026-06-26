package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistsException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.repository.PostRepository;
import vn.hoidanit.springsieutoc.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public Tag createTag(Tag inputTag) {
        if (!tagRepository.existsByName(inputTag.getName())) {
            return tagRepository.save(inputTag);
        } else {
            throw new ResourceAlreadyExistsException("Tag đã tồn tại");
        }

    }

    public List<Tag> getAllTag() {
        return tagRepository.findAll();
    }

    public Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tồn tại tag"));
    }

    public void updateTag(Tag updatedTag) {
        if (tagRepository.existsByName(updatedTag.getName())) {
            throw new ResourceAlreadyExistsException("Tag với name " +updatedTag.getName()+ " đã tồn tại");
        }

        Tag tagInDB = tagRepository.findById(updatedTag.getId()).orElseThrow(() -> new ResourceNotFoundException("Không tồn tại tag với id "+updatedTag.getId()));

        tagInDB.setName(updatedTag.getName());
        tagRepository.save(tagInDB);

    }

    public void deleteTagById(long id) {
        Tag tagToDelete = this.tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag không tồn tại với id = " + id));

        // get posts by tag
        List<Post> postsToUpdate = this.postRepository.findByTagsContains(tagToDelete);
        for (Post post : postsToUpdate) {
            post.getTags().remove(tagToDelete);

            // update post -> update post_tag
            this.postRepository.save(post);
        }

        // delete tag
        this.tagRepository.deleteById(id);
    }
}
