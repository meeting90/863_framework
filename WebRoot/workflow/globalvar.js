var baseUserURL="../workflow.user?query=";
var baseRelationURL="../workflow.relation?query=";
var baseServiceURL="../workflow.service?query=";
var baseWorkflowURL="../workflow.workflow?query=";

var getUserNameURL=baseUserURL+"getUserName&uid=";

var getFocusedUserURL=baseUserURL+"getFocusedUser&uid=";
var getAllUserURL=baseUserURL+"getAllUser&size=20&offset=";
var searchUserURL=baseUserURL+"searchUser&name=";

var getUserDetailURL=baseUserURL+"getUserDetail&uid="; 

var getAllRelationURL=baseRelationURL+"getAllRelation";
var getTrustRelationURL=baseRelationURL+"getTrustRelation";

var addRelationURL=baseRelationURL+'addRelation';

var removeRelationURL=baseRelationURL+'removeRelation';

var getWsNameURL=baseServiceURL+"getServiceName&wsid=";
var getFocusWsURL=baseServiceURL+"getFocusedService&uid=";
var getAllWsURL=baseServiceURL+"getAllService&size=20&offset=";
var getServiceURL=baseServiceURL+"getService&wsid=";
var getServiceInfoURL=baseServiceURL+"getServiceInfo&wsid=";

var addFocusServiceURL=baseServiceURL+"addFocusService";
var removeFocusServiceURL=baseServiceURL+"removeFocusService";
var updateRatingURL=baseServiceURL+"updateRating";

var searchServiceURL=baseServiceURL+"searchService&name=";
var getServiceRatingURL=baseServiceURL+"getRating&wsid=";

var getWorkflowURL=baseWorkflowURL+'getWorkflow&wfid=';
var getFocusedWfURL=baseWorkflowURL+'getFoucsedWf&uid=';
var getAllWfURL=baseWorkflowURL+"getAllWorkflow";
var getWorkflowInfoURL=baseWorkflowURL+"getWorkflowInfo&wfid=";

var focusedUser=[];
var focusedService=[];
var focusedWorkflow=[];


var userOffset=0;
var serviceOffset=0;
var workflowOffset=0;
var uid=316;
var username='Adamson';
var threshold=0.98;

var relationViewRemove;