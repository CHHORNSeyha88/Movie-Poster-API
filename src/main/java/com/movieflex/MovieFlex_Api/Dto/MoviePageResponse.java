package com.movieflex.MovieFlex_Api.Dto;

import java.util.List;

public record MoviePageResponse(
        List<MovieDto> movieDto,
        Integer pageNumber,
        Integer pageSize,
        long totalElements,
        int totalPage,
        boolean isLast
)
{

}
