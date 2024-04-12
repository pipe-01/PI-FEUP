using DomainService.Models;
using Microsoft.EntityFrameworkCore;

namespace DomainService.Data
{
    #pragma warning disable CS1591
    public class AppDBContext : DbContext
    {

        public AppDBContext(DbContextOptions<AppDBContext> opt) : base(opt)
        {

        }

        public DbSet<DomainModel> Models { get; set; }
    }
    #pragma warning restore CS1591

}
