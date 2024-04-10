package pl.luncher.v3.luncher_core.admin.model.mappers;

import org.mapstruct.*;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminCreatePlaceRequest;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminBasicPlaceResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminFullPlaceResponse;
import pl.luncher.v3.luncher_core.common.domain.Place;
import pl.luncher.v3.luncher_core.common.domain.valueobjects.Address;
import pl.luncher.v3.luncher_core.common.services.UserService;

import java.time.DayOfWeek;
import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AdminPlaceMapper {

    AdminBasicPlaceResponse mapToBasic(Place place);

    AdminFullPlaceResponse mapToFull(Place place);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", expression = "java(userService.getUserByUuid(request.getOwnerUuid()))")
    @Mapping(target = "allowedUsers",
            expression = "java(request.getAllowedUsersUuids().stream().map(userService::getUserByUuid).collect(java.util.stream.Collectors.toSet()))")
    Place mapToPlace(AdminCreatePlaceRequest request, @Context UserService userService);

    default int mapDow(DayOfWeek dow) {
        return dow.getValue();
    }

    default DayOfWeek mapDow(int dow) {
        return DayOfWeek.of(dow);
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "owner", expression = "java(userService.getUserByUuid(changes.getOwnerUuid()))")
    @Mapping(source = "placeUuid", target = "id")
    @Mapping(target = "allowedUsers", ignore = true)
    void mapToUpdate(@MappingTarget Place oldPlace, AdminUpdatePlaceRequest changes, UUID placeUuid, @Context UserService userService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateAddress(@MappingTarget Address address, Address changes);
}
