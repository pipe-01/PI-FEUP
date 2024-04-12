using Microsoft.Net.Http.Headers;
using Newtonsoft.Json.Linq;
using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Text.Json;

namespace DomainService.Security
{
    public class JwtParser
    {
        private readonly IHttpClientFactory _httpClientFactory;
        public JwtParser(IHttpClientFactory httpClientFactory) =>
        _httpClientFactory = httpClientFactory;

        public static JWTValidation ValidateToken(string jwtToken, List<int> supportedRoles)
        {
            JWTValidation validation = new JWTValidation();

            if (jwtToken == null || jwtToken.Equals("Dummy") || string.IsNullOrEmpty(jwtToken))
            {
                validation.isValid = false;
                return validation;
            }
            else
            {
                var a = GetRoleIdFromGateWay(jwtToken);
                validation.isValid = supportedRoles.Count == 0 || supportedRoles.Contains(a);
                return validation;
            }
        }

        private static readonly HttpClient client = new HttpClient();

        /// <summary>
        /// Gets RoleID From GateWay
        /// </summary>
        /// <param name="jwtToken"></param>
        /// <returns></returns>
        private static int GetRoleIdFromGateWay(string jwtToken)
        {
            var url = "https://cosn-api-gateway.herokuapp.com/getUserFromToken";
            int roleId = int.MinValue;

            var httpRequest = (HttpWebRequest)WebRequest.Create(url);
            httpRequest.Method = "POST";

            httpRequest.Headers["accept"] = "*/*";
            httpRequest.ContentType = "application/json";

            var data = JsonSerializer.Serialize(new GetUserFromTokenDTO() { token = jwtToken});

            using (var streamWriter = new StreamWriter(httpRequest.GetRequestStream()))
            {
                streamWriter.Write(data);
            }

            var httpResponse = (HttpWebResponse)httpRequest.GetResponse();
            using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
            {
                var result = streamReader.ReadToEnd();
                int.TryParse(JObject.Parse(result)["user"]["role"]["id"].ToString(), out roleId);
            }


            Console.WriteLine(httpResponse.StatusCode);

            return roleId;
        }
        
    }
    }

