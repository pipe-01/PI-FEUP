namespace DomainService.DTO
{
    /// <summary>
    /// HealthStatus For EndPoint. Unhealthy = 0, Degraded = 1, Healthy = 2
    /// </summary>
    /// <remarks>Unhealthy = 0 - Indicates that the health check determined that the component was unhealthy or an unhandled exception was thrown while executing the health check</remarks>
    /// <remarks>Degraded = 1 -Indicates that the health check determined that the component was in a degraded state.</remarks>
    /// <remarks>Healthy = 2 - Indicates that the health check determined that the component was healthy.</remarks>
    public enum HealthStatus
    {
        /// <summary>
        /// Indicates that the health check determined that the component was unhealthy,
        /// or an unhandled exception was thrown while executing the health check
        /// </summary>
        Unhealthy = 0,
        /// <summary>
        /// Indicates that the health check determined that the component was in a degraded state.
        /// </summary>
        Degraded = 1,
        /// <summary>
        /// Indicates that the health check determined that the component was healthy.
        /// </summary>
        Healthy = 2
    }
}
