namespace DomainService.KafkaLogger
{
    internal class MessageForLogging
    {
        public MessageForLogging()
        {
        }

        public string messageType { get; set; }
        public string operationType { get; set; }
        public string serviceName { get; set; }
        public string message { get; set; }
    }
}