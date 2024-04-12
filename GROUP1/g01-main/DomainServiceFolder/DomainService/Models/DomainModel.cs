using System.ComponentModel.DataAnnotations;

namespace DomainService.Models
{
    public class DomainModel
    {
        [Key]
        [Required]
        public int Id { get; set; }

        [Required]
        public string Topology { get; set; }

        [Required]
        public int ModelType { get; set; }

    }
}
