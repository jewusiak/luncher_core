Feature: Place search engine

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
      | name            | description                                      | placeTypeIdentifier | location.latitude | location.longitude |
      | The Cool Cat TR | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  |
    And response code is 200
#
#    And User creates a place as below ID 2:
#      | name              | description                                                                                                                        | placeTypeIdentifier | location.latitude | location.longitude |
#      | Restauracja Ceska | Restauracja o swobodnej atmosferze oferująca czeskie klasyki, takie jak smażony ser, dania mięsne i gulasz, a także piwo z beczki. | RESTAURANT          | 52.23165442404853 | 21.011960242452425 |
#    And response code is 200
#
#    And User creates a place as below ID 3:
#      | name          | description                                                                                         | placeTypeIdentifier | location.latitude  | location.longitude |
#      | BARdzo bardzo | Klub Muzyczny Bardzo Bardzo: Serce Warszawskiej Sceny Muzycznej. Jam Sessions, NY Funky Nights etc. | BAR                 | 52.229357307480846 | 21.017813616696706 |
#    And response code is 200
#

    And Send PUT request to /place/[ID:PLACE:1] with body as below:
  """
{
   "standardOpeningTimes": [
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

    And response code is 200
    And User logs out (by removing saved auth token)

  Scenario Outline: Opening time based places search

    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    And Refresh Hibernate Search indexes

    When Place Search request is sent as below:
      | dayOfWeek   | openAt   | size | page |
      | <dayOfWeek> | <openAt> | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size <expectedResults> in path results

    Examples:
      | dayOfWeek | openAt | expectedResults |
      | 1         | 09:59  | 0               |
      | 1         | 10:00  | 1               |
      | 1         | 22:00  | 0               |
      | 1         | 21:59  | 1               |
      | 2         | 09:59  | 0               |
      | 2         | 10:00  | 1               |
      | 2         | 23:59  | 1               |
      | 3         | 00:01  | 1               |
      | 3         | 01:59  | 1               |
      | 3         | 02:00  | 0               |
      | 3         | 09:59  | 0               |
      | 3         | 10:00  | 1               |
      | 3         | 22:00  | 0               |
      | 4         | 09:59  | 0               |
      | 4         | 10:00  | 1               |
      | 4         | 22:00  | 0               |
      | 5         | 09:59  | 0               |
      | 5         | 10:00  | 1               |
      | 5         | 22:59  | 1               |
      | 5         | 23:00  | 0               |
      | 6         | 08:59  | 0               |
      | 6         | 09:00  | 1               |
      | 6         | 23:00  | 0               |
      | 7         | 08:59  | 0               |
      | 7         | 09:00  | 1               |
      | 7         | 23:59  | 1               |
      | 1         | 00:01  | 1               |
      | 1         | 02:59  | 1               |
      | 1         | 03:00  | 0               |
