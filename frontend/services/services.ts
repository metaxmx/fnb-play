import {AuthenticationService} from "./authentication.service"
import {HttpCommunicationService} from "./http-communication.service"
import {ForumService} from "./forum.service"
import {ThreadService} from "./thread.service"

export const FNB_SERVICE_PROVIDERS = [
    AuthenticationService,
    HttpCommunicationService,
    ForumService,
    ThreadService
];