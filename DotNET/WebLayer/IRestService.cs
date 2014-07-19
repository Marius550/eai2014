using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Web.Script.Services;
using WebLayer.Messages;

namespace WebLayer
{
    [ServiceContract]
    public interface IRestService
    {
        /// <summary>
        /// Returns a list of all drugs in the database.
        /// </summary>
        /// <returns>The drug list</returns>
        [OperationContract]
        [WebGet(UriTemplate = "drug", ResponseFormat = WebMessageFormat.Json)]
        MessageDrug[] Drugs();

        /// <summary>
        /// Creates a new drug.
        /// </summary>
        /// <param name="drug">The new drug</param>
        /// <returns>The new created drug</returns>
        [OperationContract]
        [WebInvoke(UriTemplate = "drug/create", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, Method = "POST")]
        MessageDrug CreateDrug(MessageDrug drug);

        /// <summary>
        /// Returns a single drug.
        /// </summary>
        /// <param name="pzn">The pzn of the drug</param>
        /// <returns>The drug</returns>
        [OperationContract]
        [WebGet(UriTemplate = "drug/{pzn}", ResponseFormat = WebMessageFormat.Json)]
        MessageDrug Drug(string pzn);        

        /// <summary>
        /// Updates the master data of a drug.
        /// </summary>
        /// <param name="drug">The new drug (data)</param>
        /// <param name="pzn">The pzn of the drug</param>
        /// <returns>The updated drug</returns>
        [OperationContract]
        [WebInvoke(UriTemplate = "drug/{pzn}/update", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, Method = "POST")] // PUT is not working
        MessageDrug UpdateDrug(MessageDrug drug, string pzn);

        /// <summary>
        /// Updates a list of drugs
        /// </summary>
        /// <param name="drugs">The drug list</param>
        [OperationContract]
        [WebInvoke(UriTemplate = "drug/update/batch", RequestFormat = WebMessageFormat.Json, Method = "POST")] // PUT is not working
        void UpdateDrugs(MessageDrug[] drugs);

        /// <summary>
        /// Creates multiple drugs at once.
        /// </summary>
        /// <param name="drugs">The list of drugs</param>
        [OperationContract]
        [WebInvoke(UriTemplate = "drug/create/batch", RequestFormat = WebMessageFormat.Json, Method = "POST")]
        void CreateDrugs(MessageDrug[] drugs);

        [OperationContract]
        [WebGet(UriTemplate = "statistic", ResponseFormat = WebMessageFormat.Json)]
        MessageStatistic GetStatistic();

        [OperationContract]
        [WebGet(UriTemplate = "statistic/{start}/{end}", ResponseFormat = WebMessageFormat.Json)]
        MessageStatistic GetStatisticInTimeSpan(string start, string end);
    }
}
