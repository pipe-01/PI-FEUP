using AutoMapper;
using DomainService.Models;

namespace DomainService.Profiles
{
    public class DomainProfile : Profile
    {
        public DomainProfile()
        {
            CreateMap<DomainModel, DomainModelDTO>();
            CreateMap<DomainModelDTO, DomainModel>();

        }

    }
}
