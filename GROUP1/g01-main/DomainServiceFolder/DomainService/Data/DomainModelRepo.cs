using DomainService.Models;

namespace DomainService.Data
{
    public class DomainModelRepo : IDomainModelRepo
    {
        private readonly AppDBContext _context;

        public DomainModelRepo(AppDBContext dBContext)
        {
            _context = dBContext;
        }

        public void CreateDomain(DomainModel domain)
        {
            if(domain == null)
            {
                throw new ArgumentNullException(nameof(domain));
            }

            _context.Models.Add(domain);
        }

        public void Delete(DomainModel domainmodelItem)
        {
            _context.Models.Remove(domainmodelItem);
        }

        public IEnumerable<DomainModel> GetAllDomains() => _context.Models.ToList();

        public DomainModel GetDomainById(int id) => _context.Models.FirstOrDefault(p => p.Id == id);
        

        public bool SaveChanges()
        {
            return (_context.SaveChanges() >= 0);
        }

        IEnumerable<DomainModel> IDomainModelRepo.GetDomainByType(int type)
        {
            return _context.Models.ToList().Where(p => p.ModelType == type);
        }

        DomainModel IDomainModelRepo.UpdateDomainModel(DomainModel domainmodelItem)
        {
            _context.Models.Update(domainmodelItem);
            return domainmodelItem;
        }
    }
}
