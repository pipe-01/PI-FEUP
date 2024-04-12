using DomainService.Models;

namespace DomainService.Data
{
    public interface IDomainModelRepo
    {
        bool SaveChanges();

        IEnumerable<DomainModel> GetAllDomains();

        DomainModel GetDomainById(int id);

        void CreateDomain(DomainModel domain);
        DomainModel UpdateDomainModel(DomainModel domainmodelItem);
        IEnumerable<DomainModel> GetDomainByType(int type);
        void Delete(DomainModel domainmodelItem);
    }
}
