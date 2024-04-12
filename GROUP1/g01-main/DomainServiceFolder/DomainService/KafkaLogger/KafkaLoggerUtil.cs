using Confluent.Kafka;
using System.Text.Json;

namespace DomainService.KafkaLogger
{
    public class KafkaLoggerUtil
    {
        public static async Task SendDestributedLogAsync(OperationTypeENUM operationType, string v, bool isError = false)
        {
            string bootstrapServers = "feupcosn2122.fe.up.pt:9092";
            string topicName = "cosn_logging";
            string groupId = "my-app";

            var config = new ClientConfig();
            config.BootstrapServers = bootstrapServers;
            config.ClientId = groupId;


            Console.WriteLine($"{nameof(KafkaLoggerUtil)} starting");

            var msg = new MessageForLogging() {
                messageType = isError ? "FATAL" : "INFORMATION",
                operationType = operationType.ToString(),
                serviceName = "DOMAIN_MODEL_SERVICE",
                message = $"A user as actioned the endpoint {v} and it completed successfully"
            };

            try
            {
                using (var producer = new ProducerBuilder<Null, string>(config).Build())
                {
                    var a = await producer.ProduceAsync(topicName, new Message<Null, string> { Value = JsonSerializer.Serialize(msg) });
                }

            }catch (Exception ex)
            {
                Console.WriteLine($"{nameof(KafkaLoggerUtil)} Error");
            }
            finally
            {
                Console.WriteLine($"{nameof(KafkaLoggerUtil)} End");

            }

        }
    }
}
