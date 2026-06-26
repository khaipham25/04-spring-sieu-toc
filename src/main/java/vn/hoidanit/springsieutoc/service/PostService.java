package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.PostRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.PostResponseDTO;
import vn.hoidanit.springsieutoc.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post convertDTOtoPost(PostRequestDTO dto) {
        Post p = new Post();
        p.setTitle(dto.getTitle());
        p.setContent(dto.getContent());
        List<Tag> tags = dto.getTags().stream().map(inputTag -> {
            return new Tag(inputTag.getId(), inputTag.getName(), null);
        }).collect(Collectors.toList());

        p.setTags(tags);

        User u = new User();
        u.setId(dto.getUser().getId());
        p.setUser(u);

        return p;
    }

    public PostResponseDTO convertPostToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());

        List<PostResponseDTO.OutputTag> tags = post.getTags().stream().map(tag -> {
            PostResponseDTO.OutputTag t = new PostResponseDTO.OutputTag();
            t.setId(tag.getId());
            t.setName(tag.getName());
            return t;
        }).collect(Collectors.toList());

        dto.setTags(tags);

        return dto;
    }

    public PostResponseDTO createPost(PostRequestDTO inputPost) {
        Post post = postRepository.save(convertDTOtoPost(inputPost));

        return convertPostToDTO(post);
    }

    public List<PostResponseDTO> fetchPosts() {
        return this.postRepository.findAll().stream().map(post -> {
            return convertPostToDTO(post);
        }).collect(Collectors.toList());
    }

    public PostResponseDTO fetchPostById(Long id) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post không tồn tại."));
        return convertPostToDTO(post);
    }

    public PostResponseDTO updatePostById(Long id, PostRequestDTO postDTO) {
        Post postInDB = this.postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post không tồn tại."));

        postInDB.setTitle(postDTO.getTitle());
        postInDB.setContent(postDTO.getContent());
        if (postDTO.getTags().size() > 0) {
            List<Tag> tags = postDTO.getTags().stream().map(inputTag -> {
                return new Tag(inputTag.getId(), inputTag.getName(), null);
            }).collect(Collectors.toList());

            postInDB.setTags(tags);
        }

        this.postRepository.save(postInDB);

        return convertPostToDTO(postInDB);
    }

    public void deletePostById(Long id) {
        this.postRepository.deleteById(id);
    }
}
