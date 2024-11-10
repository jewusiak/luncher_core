Feature: Place search based on opening times

  Background:
    Given User exists:
      | uuid                                 | email                  | password | name     | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp      | 1234     | Zbigniew | Json    | USER         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp  | 1234     | MAN      | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | rmanager2@luncher.corp | 1234     | MAN2     | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin@luncher.corp     | 1234     | ADMIN    | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp       | 1234     | MOD      | Json2   | SYS_MOD      |

    And place types exist:
      | identifier | iconName   | name       |
      | RESTAURANT | restaurant | Restaurant |
      | BAR        | bar        | Bar        |

    And User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below ID 1:
      | name            | description                                      | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | The Cool Cat TR | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200

    And Send PUT request to /place/[ID:PLACE:1] with body as below:
  """
{
   "openingWindows": [
      {
         "startTime": {
            "day": "MONDAY",
            "time": "10:00"
         },
         "endTime": {
            "day": "MONDAY",
            "time": "22:00"
         }
      },
      {
         "startTime": {
            "day": "TUESDAY",
            "time": "10:00"
         },
         "endTime": {
            "day": "WEDNESDAY",
            "time": "02:00"
         }
      },
      {
         "startTime": {
            "day": "WEDNESDAY",
            "time": "10:00"
         },
         "endTime": {
            "day": "WEDNESDAY",
            "time": "22:00"
         }
      },
      {
         "startTime": {
            "day": "THURSDAY",
            "time": "10:00"
         },
         "endTime": {
            "day": "THURSDAY",
            "time": "22:00"
         }
      },
      {
         "startTime": {
            "day": "FRIDAY",
            "time": "10:00"
         },
         "endTime": {
            "day": "FRIDAY",
            "time": "23:00"
         }
      },
      {
         "startTime": {
            "day": "SATURDAY",
            "time": "09:00"
         },
         "endTime": {
            "day": "SATURDAY",
            "time": "23:00"
         }
      },
      {
         "startTime": {
            "day": "SUNDAY",
            "time": "09:00"
         },
         "endTime": {
            "day": "MONDAY",
            "time": "03:00"
         }
      }
   ]
}
    """

    And Refresh Hibernate Search indexes

    And response code is 200
    And User logs out (by removing saved auth token)

  Scenario Outline: Opening time based places search

    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp


    When Place Search request is sent as below:
      | openAt.day  | openAt.time | size | page |
      | <dayOfWeek> | <openAt>    | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size <expectedResults> in path .

    Examples:
      | dayOfWeek | openAt | expectedResults |
      | MONDAY    | 09:59  | 0               |
      | MONDAY    | 10:00  | 1               |
      | MONDAY    | 22:00  | 0               |
      | MONDAY    | 21:59  | 1               |
      | TUESDAY   | 09:59  | 0               |
      | TUESDAY   | 10:00  | 1               |
      | TUESDAY   | 23:59  | 1               |
      | WEDNESDAY | 00:01  | 1               |
      | WEDNESDAY | 01:59  | 1               |
      | WEDNESDAY | 02:00  | 0               |
      | WEDNESDAY | 09:59  | 0               |
      | WEDNESDAY | 10:00  | 1               |
      | WEDNESDAY | 22:00  | 0               |
      | THURSDAY  | 09:59  | 0               |
      | THURSDAY  | 10:00  | 1               |
      | THURSDAY  | 22:00  | 0               |
      | FRIDAY    | 09:59  | 0               |
      | FRIDAY    | 10:00  | 1               |
      | FRIDAY    | 22:59  | 1               |
      | FRIDAY    | 23:00  | 0               |
      | SATURDAY  | 08:59  | 0               |
      | SATURDAY  | 09:00  | 1               |
      | SATURDAY  | 23:00  | 0               |
      | SUNDAY    | 08:59  | 0               |
      | SUNDAY    | 09:00  | 1               |
      | SUNDAY    | 23:59  | 1               |
      | MONDAY    | 00:01  | 1               |
      | MONDAY    | 02:59  | 1               |
      | MONDAY    | 03:00  | 0               |
