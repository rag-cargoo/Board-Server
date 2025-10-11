package com.example.boardserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.boardserver.dto.UserDTO;

@Mapper
public interface UserProfileMapper {
    UserDTO getUserProfile(@Param("userId") String userId);

    int insertUserProfile(UserDTO userProfile);

    int deleteUserProfile(@Param("userId") String userId);

    UserDTO findByIdAndPassword(@Param("userId") String userId, @Param("password") String password);

    int idCheck(@Param("userId") String userId);

    int updatePassword(@Param("userId") String userId, @Param("password") String password);

    int updateAddress(UserDTO user);
}
