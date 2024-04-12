using System.ComponentModel.DataAnnotations;

namespace DomainService.Models
{
    public class DomainModelDTO
    {
        public int Id { get; set; }

        public string Topology { get; set; }

        public int ModelType { get; set; }

    }
}
