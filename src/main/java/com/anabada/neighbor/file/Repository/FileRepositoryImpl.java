package com.anabada.neighbor.file.Repository;

import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileRepositoryImpl extends FileRepository {
    @Override
    @Insert("insert into file(" +
            "id, postId, originalName, saveName, size, deleteYn, createdDate, deletedDate)" +
            "values(#{id}, #{postId}, #{originalName}, #{saveName}, #{size}, 0, now(), null)")
    void insertFile(FileRequest fileRequest);

    @Override
    @Select("select * from file where deleteYn = 0 and postId = #{postId} order by id")
    List<FileResponse> selectFilesByPostId(Long postId);

    @Override
    @Select("select * from file" +
            " where deleteYn = 0 and id = #{id} order by id")
    FileResponse selectFileById(Long id);

    @Override
    @Delete("update file set deleteYn = 1, deletedDate = NOW()" +
            " where id IN (#{id})")
    void deleteFileById(Long id);
}
