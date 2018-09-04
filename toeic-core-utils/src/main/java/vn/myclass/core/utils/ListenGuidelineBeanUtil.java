package vn.myclass.core.utils;

import vn.myclass.core.dto.ListenGuidelineDTO;
import vn.myclass.core.persistence.entity.ListenGuidelineEntity;

public class ListenGuidelineBeanUtil {
    public static ListenGuidelineDTO entityToDTO(ListenGuidelineEntity entity) {
        ListenGuidelineDTO dto = new ListenGuidelineDTO();
        dto.setListenGuidelineId(entity.getListenGuidelineId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setImage(entity.getImage());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedDate(entity.getModifiedDate());
        return dto;
    }

    public static ListenGuidelineEntity dtoToEntity(ListenGuidelineDTO dto) {
        ListenGuidelineEntity entity = new ListenGuidelineEntity();
        entity.setListenGuidelineId(dto.getListenGuidelineId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setImage(dto.getImage());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setModifiedDate(dto.getModifiedDate());
        return entity;
    }
}
