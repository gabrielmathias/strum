'use strict';

describe('Controller Tests', function() {

    describe('Acceptance Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAcceptance, MockUser, MockStory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAcceptance = jasmine.createSpy('MockAcceptance');
            MockUser = jasmine.createSpy('MockUser');
            MockStory = jasmine.createSpy('MockStory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Acceptance': MockAcceptance,
                'User': MockUser,
                'Story': MockStory
            };
            createController = function() {
                $injector.get('$controller')("AcceptanceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'strumApp:acceptanceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
