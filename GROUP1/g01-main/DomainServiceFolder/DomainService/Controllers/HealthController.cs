using AutoMapper;
using DomainService.Data;
using DomainService.DTO;
using DomainService.Health;
using DomainService.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Net;

namespace DomainService.Controllers
{
    [Route("api/health")]
    [ApiController]
    [Produces("application/json")]
    public class HealthController : ControllerBase
    {
        /// <summary>
        /// Get Health. Due to limitations on .NetCore 6 -> use /health and not /api/health
        /// </summary>
        /// <remarks>Provides an indication about the health of the API based on CPU Usage</remarks>
        /// <response code="200">API is healthy</response>
        /// <response code="503">API is unhealthy or in degraded state</response>
        [HttpGet]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status503ServiceUnavailable)]
        public async Task<HealthStatus> Get()
        {
            var client = new MemoryMetricsClient();
            var metrics = client.GetMetrics();
            var percentUsed = 100 * metrics.Used / metrics.Total;

            var status = HealthStatus.Healthy;
            if (percentUsed > 80)
            {
                status = HealthStatus.Degraded;
            }
            if (percentUsed > 90)
            {
                status = HealthStatus.Unhealthy;
            }

            return status;
        }
    }
}
