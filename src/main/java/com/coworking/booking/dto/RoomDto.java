package com.coworking.booking.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JacksonXmlRootElement(localName = "room")
public class RoomDto {

    @JacksonXmlProperty
    private Long id;

    @JacksonXmlProperty(localName = "name")
    private String roomName;

    @JacksonXmlProperty(localName = "seats")
    private Integer seats;

    @JacksonXmlProperty
    private Boolean available;

    @JacksonXmlProperty
    private Long workspaceId;
}
