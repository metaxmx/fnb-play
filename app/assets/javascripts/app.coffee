
dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'textAngular',
    'fnbApp.filters',
    'fnbApp.services',
    'fnbApp.controllers',
    'fnbApp.directives',
    'fnbApp.common',
    'fnbApp.routeConfig'
]

app = angular.module('fnbApp', dependencies)

angular.module('fnbApp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/listforums.html'
            })
            .when('/forum/:id', {
                templateUrl: '/assets/partials/showforum.html'
            })
            .when('/forum/:id/newtopic', {
                templateUrl: '/assets/partials/newtopic.html'
            })
            .when('/topic/:id', {
                templateUrl: '/assets/partials/showtopic.html'
            })
            .when('/login', {
                templateUrl: '/assets/partials/login.html'
            })
            .when('/admin/forum/configure', {
                templateUrl: '/assets/partials/configureforums.html'
            })
            .otherwise({redirectTo: '/'})
    .config ($locationProvider) ->
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        })

@commonModule = angular.module('fnbApp.common', [])
@controllersModule = angular.module('fnbApp.controllers', [])
@servicesModule = angular.module('fnbApp.services', [])
@modelsModule = angular.module('fnbApp.models', [])
@directivesModule = angular.module('fnbApp.directives', [])
@filtersModule = angular.module('fnbApp.filters', [])