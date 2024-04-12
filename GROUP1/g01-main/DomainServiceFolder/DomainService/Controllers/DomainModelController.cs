using AutoMapper;
using DomainService.Data;
using DomainService.KafkaLogger;
using DomainService.Models;
using DomainService.Security;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Net.Http.Headers;

namespace DomainService.Controllers
{
    /// <summary>
    /// Domain Service Controller - Responsible for rooting
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    [Produces("application/json")]
    public class DomainModelController : ControllerBase
    {
        private readonly IDomainModelRepo _repository;
        private readonly IMapper _mapper;

        public DomainModelController(IDomainModelRepo repo, IMapper mapper)
        {
            _repository = repo;
            _mapper = mapper;
        }

        #region RQ1:I want to C/U/D a domain model in the form of a graph with the JSON format

        /// <summary>
        /// Creates the Domain Model
        /// </summary>
        /// <param name="domainmodelCreateDto">Domain Model DTO</param>
        /// <returns></returns>
        /// <response code="201">Returns the newly created item</response>
        /// <response code="400">If the item is null</response>
        [HttpPost]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        public async Task<ActionResult<DomainModelDTO>> CreateDomainModelAsync(DomainModelDTO domainmodelCreateDto)
        {
            await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.CREATE, "CreateDomainModel");
            var _bearer_token = Request.Headers[HeaderNames.Authorization].ToString().Replace("Bearer ", "") ?? "Dummy";
            if (JwtParser.ValidateToken(_bearer_token, new List<int>() { 1, 2, 3 }).isValid == false)
            {
                return new UnauthorizedObjectResult("Invalid token or access level");
            }

            DomainModel domainmodelModel = new DomainModel();
            domainmodelModel.Topology = domainmodelCreateDto.Topology;
            domainmodelModel.ModelType = domainmodelCreateDto.ModelType;

            _repository.CreateDomain(domainmodelModel);
            _repository.SaveChanges();

            var domainmodelReadDto = _mapper.Map<DomainModelDTO>(domainmodelModel);
            return CreatedAtRoute(nameof(GetdomainmodelByIdAsync), new { Id = domainmodelReadDto.Id }, domainmodelReadDto);
        }

        /// <summary>
        /// Updates a already created domain model
        /// </summary>
        /// <param name="id">The id</param>
        /// <param name="domainmodelCreateDto">The DTO</param>
        /// <returns></returns>
        /// <response code="20'">Ok update</response>
        [HttpPut("{id:int}",Name = "UpdatedomainmodelById")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        public async Task<ActionResult<DomainModelDTO>> UpdateDomainModelAsync(int id, DomainModelDTO domainmodelCreateDto)
        {
            var _bearer_token = Request.Headers[HeaderNames.Authorization].ToString().Replace("Bearer ", "") ?? "Dummy";
            if (JwtParser.ValidateToken(_bearer_token, new List<int>() { 1, 2, 3 }).isValid == false)
            {
                return new UnauthorizedObjectResult("Invalid token or access level");
            }

            if (id != domainmodelCreateDto.Id)
            {
                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.UPDATE, "UpdateDomainModel", true);
                return BadRequest("DomainModel ID mismatch");
            }

            DomainModel domainmodelModel = _mapper.Map<DomainModel>(domainmodelCreateDto);
            var domainmodelItem = _repository.GetDomainById(domainmodelCreateDto.Id);
            if (domainmodelItem != null)
            {
                domainmodelItem.ModelType = domainmodelModel.ModelType;
                domainmodelItem.Topology = domainmodelModel.Topology;
                _repository.UpdateDomainModel(domainmodelItem);
                _repository.SaveChanges();

                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.UPDATE, "UpdateDomainModel");
                return Ok(_mapper.Map<DomainModelDTO>(domainmodelItem));
            }
            else
            {
                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.UPDATE, "UpdateDomainModel", true);
                return NotFound($"DomainModel with Id = {id} not found");
            }
        }

        /// <summary>
        /// Delete a operation
        /// Should all users be able to update and delete domain models created by others? Or
        /// only users that created a domain model can update and/delete it?
        ///   -> Pertence ao user, outros outros podem ver/copiar
        ///   -> Não pode apagar
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpDelete("{id:int}")]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        public async Task<ActionResult<DomainModelDTO>> DeleteDomainModelAsync(int id)
        {
            var _bearer_token = Request.Headers[HeaderNames.Authorization].ToString().Replace("Bearer ", "") ?? "Dummy";
            if (JwtParser.ValidateToken(_bearer_token, new List<int>() { 1, 2, 3 }).isValid == false)
            {
                return new UnauthorizedObjectResult("Invalid token or access level");
            }

            await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.DELETE, "DeleteDomainModel");
            var domainmodelItem = _repository.GetDomainById(id);
            if (domainmodelItem != null)
            {
                _repository.Delete(domainmodelItem);
                _repository.SaveChanges();

                return Ok();
            }
            else
            {
                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.DELETE, "DeleteDomainModel", true);
                return NotFound($"DeleteDomainModel with Id = {id} not found");
            }
        }

        #endregion

        #region RQ2: As a user, I want to read a domain model in the form of a graph with the JSON format.

        /// <summary>
        /// Gets all domains
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        public async Task<ActionResult<IEnumerable<DomainModelDTO>>> GetDomainsAsync()
        {
            Console.WriteLine("-> GetDomains");
            var _bearer_token = Request.Headers[HeaderNames.Authorization].ToString().Replace("Bearer ", "") ?? "Dummy";
            if (JwtParser.ValidateToken(_bearer_token, new List<int>()).isValid == true)
            {
                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.READ, "-> GetDomains");
                return new OkObjectResult(_mapper.Map<IEnumerable<DomainModelDTO>>(_repository.GetAllDomains()));
            }
            else
            {
                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.READ, "-> GetDomains", true);
                return new UnauthorizedObjectResult("Invalid token");
            }
        }

        /// <summary>
        /// Gets domains by Id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet("GetdomainmodelById/{id:int}", Name = "GetdomainmodelById")]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        public async Task<ActionResult<DomainModelDTO>> GetdomainmodelByIdAsync(int id)
        {
            var _bearer_token = Request.Headers[HeaderNames.Authorization].ToString().Replace("Bearer ", "") ?? "Dummy";
            if (JwtParser.ValidateToken(_bearer_token, new List<int>()).isValid == false)
            {
                return new UnauthorizedObjectResult("Invalid token or access level");
            }

            var domainmodelItem = _repository.GetDomainById(id);
            await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.READ, "GetdomainmodelById");
            if (domainmodelItem != null)
            {
                return Ok(_mapper.Map<DomainModelDTO>(domainmodelItem));
            }

            await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.READ, "GetdomainmodelById", true);
            return NotFound();
        }
        #endregion

        #region RQ3: As a user, I want to read all existing domain models of a given type (macroscopic,microscopic, mesoscopic)
        /// <summary>
        /// Gets all existing domain models of a given type (macroscopic,microscopic, mesoscopic)
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        [HttpGet("GetdomainmodelByType/{type}", Name = "GetdomainmodelByType")]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        public async Task<ActionResult<IEnumerable<DomainModelDTO>>> GetdomainmodelByType(int type)
        {
            var _bearer_token = Request.Headers[HeaderNames.Authorization].ToString().Replace("Bearer ", "") ?? "Dummy";
            if (JwtParser.ValidateToken(_bearer_token, new List<int>()).isValid == false)
            {
                return new UnauthorizedObjectResult("Invalid token or access level");
            }

            if (type < 1 || type > 3)
            {
                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.READ, "GetdomainmodelByType :: Invalid Type ::", true);
                return NotFound("Invalid DomainModel Type. Must be: 0-macroscopic,1-microscopic,2-mesoscopic");

            }

            var domainmodelItem = _repository.GetDomainByType(type);
            if (domainmodelItem != null)
            {
                await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.READ, "GetdomainmodelByType");
                return Ok(_mapper.Map<IEnumerable<DomainModelDTO>>(domainmodelItem));
            }

            await KafkaLoggerUtil.SendDestributedLogAsync(OperationTypeENUM.READ, "GetdomainmodelByType", true);
            return NotFound();
        }
        #endregion
    }
}
