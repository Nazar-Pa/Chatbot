// //const handler = require('../handler/handler');
// //const { WebhookClient } = require('dialogflow-fulfillment');

// // 
// const motivatioList = ['Motivation 1', 'Motivation 2', 'Motivation 3', , 'Motivation 4', 'Motivation 5', 'Motivation 6', 'Motivation 7', 'Motivation 8'];

// // for (var a = [0, 1, 2, 3, 4, 5, 6, 7], i = a.length; i--; ) {
// //     var random = a.splice(Math.floor(Math.random() * (i + 1)), 1)[0]; 
// // }
// // console.log(random);

// /**
//  * Intent to send UI controls to Android
//  */
// // const androidIntent = (agent) => {

// //     //const params = { "template": "text" };
// //     const params = { "template": "button", "buttonItems": [{ "uiText": "Give motivation", "actionText": "action 1 selected", "isPositive": false }, 
// //     { "uiText": "Action 2", "actionText": "action 2 selected", "isPositive": false }, { "uiText": "Action 3", "actionText": "action 3 selected", "isPositive": false },
// //     { "uiText": "Action 4", "actionText": "action 4 selected", "isPositive": false }], "align": "v", "size": "s", "eventToCall": "android_event" };
// //     //const params = { "template": "checkbox", "items": [{ "uiText": "item 1<br> this item is best", "id": "1" }, { "uiText": "item 2<br> this item is OK", "id": "2" }, { "uiText": "item 3", "id": "3" }], "buttonItems": [{ "uiText": "Yes", "actionText": "process selected", "isPositive": true }, { "uiText": "No", "actionText": "cancel", "isPositive": false }], "align": "h", "size": "l", "eventToCall": "android_event" };
// //     //const params = { "template": "card", "cardItems": { "imgUrl": "https://picsum.photos/seed/picsum/900/500", "title": "<b>Image Title</b><i>(optional)</i>", "description": "<i>Image description. (optional)</i>" }, "buttonItems": [{ "uiText": "Yes", "actionText": "process selected", "isPositive": true }, { "uiText": "No", "actionText": "cancel", "isPositive": false }], "align": "h", "size": "l", "eventToCall": "android_event" };
// //     //const params = { "template": "hyperlink", "linkItems": [{ "uiText": "Next Activity", "linkType": "internal", "navigateAndroid": "com.tyagiabhinav.dialogflowchat.NavTestActivity", "navigateIOS": "", "isPositive": true }, { "uiText": "Google", "linkType": "external", "navigateAndroid": "http://www.google.com", "navigateIOS": "http://www.google.com", "isPositive": false }], "align": "v", "size": "l" };
// //     const param_context = { name: "param_context", lifespan: 10, parameters: params };
// //     agent.context.set(param_context);
// //     agent.add('You can ask me about below tasks');
// // };

// const WelcomeIntent = (agent) => {

//     //const params = { "template": "text" };
//     const params = {
//         "template": "button", "buttonItems": [{ "uiText": "Give motivation", "actionText": "action 1 selected", "isPositive": false },
//         { "uiText": "Action 2", "actionText": "action 2 selected", "isPositive": false }, { "uiText": "Action 3", "actionText": "action 3 selected", "isPositive": false },
//         { "uiText": "Action 4", "actionText": "action 4 selected", "isPositive": false }], "align": "v", "size": "s", "eventToCall": "android_event"
//     };
//     //const params = { "template": "checkbox", "items": [{ "uiText": "item 1<br> this item is best", "id": "1" }, { "uiText": "item 2<br> this item is OK", "id": "2" }, { "uiText": "item 3", "id": "3" }], "buttonItems": [{ "uiText": "Yes", "actionText": "process selected", "isPositive": true }, { "uiText": "No", "actionText": "cancel", "isPositive": false }], "align": "h", "size": "l", "eventToCall": "android_event" };
//     //const params = { "template": "card", "cardItems": { "imgUrl": "https://picsum.photos/seed/picsum/900/500", "title": "<b>Image Title</b><i>(optional)</i>", "description": "<i>Image description. (optional)</i>" }, "buttonItems": [{ "uiText": "Yes", "actionText": "process selected", "isPositive": true }, { "uiText": "No", "actionText": "cancel", "isPositive": false }], "align": "h", "size": "l", "eventToCall": "android_event" };
//     //const params = { "template": "hyperlink", "linkItems": [{ "uiText": "Next Activity", "linkType": "internal", "navigateAndroid": "com.tyagiabhinav.dialogflowchat.NavTestActivity", "navigateIOS": "", "isPositive": true }, { "uiText": "Google", "linkType": "external", "navigateAndroid": "http://www.google.com", "navigateIOS": "http://www.google.com", "isPositive": false }], "align": "v", "size": "l" };
//     const param_context = { name: "param_context", lifespan: 10, parameters: params };
//     agent.context.set(param_context);
//     agent.add('hi am your your personal virtual assistant, here are my capabilities. What is your name?');
// };

// const getWeather = (agent) => {
//     const params = { "template": "text" };
//     const param_context = { name: "param_context2", lifespan: 10, parameters: params };
//     agent.context.set(param_context);
//     agent.add("body");
// };

// const activateMotivation = (agent) => {
//     const params = { "template": "text" };
//     const param_context = { name: "param_context3", lifespan: 10, parameters: params };
//     agent.context.set(param_context);
//     // randomNum = Math.floor(Math.random()*3);
//     for (var a = [0, 1, 2, 3, 4, 6, 7], i = a.length; i--;) {
//         var random = a.splice(Math.floor(Math.random() * (i + 1)), 1)[0];

//     }
//     const motivation = motivatioList[random]
//     agent.add(JSON.stringify(motivation));
// };

// /**
//  * Intent to send UI controls to Android
//  */
// const androidEvent = (agent) => {
//     const params = { "template": "text" };
//     const param_context = { name: "param_context", lifespan: 10, parameters: params };
//     agent.context.set(param_context);
//     agent.add('Event captured successfully!');
// };

// module.exports = {
//     WelcomeIntent,
//     androidEvent,
//     getWeather,
//     activateMotivation
// };
