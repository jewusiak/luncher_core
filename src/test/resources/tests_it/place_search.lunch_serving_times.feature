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
      | name            | description                                      | placeTypeIdentifier | location.latitude | location.longitude |
      | The Cool Cat TR | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  |
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

    And Send POST request to /place/[ID:PLACE:1]/menuoffer with body as below:
    """
    {
      "menuOffer": {
        "name": "Lunch Monday Special",
        "basePrice": {
          "amount": 39.99,
          "currencyCode": "PLN"
        },
        "parts": [
          {
            "name": "First Course",
            "required": true,
            "supplement": {
              "amount": 0,
              "currencyCode": "PLN"
            },
            "options": [
              {
                "name": "Tomato Soup",
                "description": "Classic tomato soup with basil and cream",
                "supplement": {
                  "amount": 0,
                  "currencyCode": "PLN"
                },
                "required": false
              },
              {
                "name": "Tripe Soup",
                "description": "Traditional Polish tripe soup",
                "supplement": {
                  "amount": 2.5,
                  "currencyCode": "PLN"
                },
                "required": false
              }
            ]
          },
          {
            "name": "Main Course",
            "required": true,
            "supplement": {
              "amount": 0,
              "currencyCode": "PLN"
            },
            "options": [
              {
                "name": "Grilled Chicken Breast",
                "description": "Served with mashed potatoes and steamed vegetables",
                "supplement": {
                  "amount": 0,
                  "currencyCode": "PLN"
                },
                "required": false
              },
              {
                "name": "Pork Schnitzel",
                "description": "Breaded pork cutlet served with fries and salad",
                "supplement": {
                  "amount": 3,
                  "currencyCode": "PLN"
                },
                "required": false
              }
            ]
          },
          {
            "name": "Dessert",
            "required": false,
            "supplement": {
              "amount": 5,
              "currencyCode": "PLN"
            },
            "options": [
              {
                "name": "Apple Pie",
                "description": "Warm apple pie with vanilla ice cream",
                "supplement": {
                  "amount": 0,
                  "currencyCode": "PLN"
                },
                "required": false
              },
              {
                "name": "Cheesecake",
                "description": "Creamy cheesecake with raspberry sauce",
                "supplement": {
                  "amount": 1.5,
                  "currencyCode": "PLN"
                },
                "required": false
              }
            ]
          }
        ],
        "recurringServingRanges": [
          {
            "startTime": {
              "time": "11:00",
              "day": "MONDAY"
            },
            "endTime": {
              "time": "14:00",
              "day": "MONDAY"
            }
          },
          {
            "startTime": {
              "time": "11:00",
              "day": "TUESDAY"
            },
            "endTime": {
              "time": "14:00",
              "day": "TUESDAY"
            }
          },
          {
            "startTime": {
              "time": "11:00",
              "day": "SUNDAY"
            },
            "endTime": {
              "time": "01:00",
              "day": "MONDAY"
            }
          }
        ],
        "oneTimeServingRanges": [
          {
            "startTime": "2024-10-21T10:00:00",
            "endTime": "2024-10-21T20:34:00"
          },
          {
            "startTime": "2024-10-30T08:00:00",
            "endTime": "2024-10-30T09:34:00"
          },
          {
            "startTime": "2024-11-15T22:55:00",
            "endTime": "2024-11-16T22:56:00"
          }
        ]
      }
    }
    """
    Then response code is 200
    And Send GET request to /place/[ID:PLACE:1] without body
    And response code is 200
    And HTTP Response is:
      | menuOffers[0].name   | menuOffers[0].basePrice.amount | menuOffers[0].basePrice.currencyCode | menuOffers[0].parts[0].name | menuOffers[0].parts[0].required | menuOffers[0].parts[0].supplement.amount | menuOffers[0].parts[0].supplement.currencyCode | menuOffers[0].parts[0].options[0].name | menuOffers[0].parts[0].options[0].description | menuOffers[0].parts[0].options[0].supplement.amount | menuOffers[0].parts[0].options[0].supplement.currencyCode | menuOffers[0].parts[0].options[0].required | menuOffers[0].parts[0].options[1].name | menuOffers[0].parts[0].options[1].description | menuOffers[0].parts[0].options[1].supplement.amount | menuOffers[0].parts[0].options[1].supplement.currencyCode | menuOffers[0].parts[0].options[1].required | menuOffers[0].parts[1].name | menuOffers[0].parts[1].required | menuOffers[0].parts[1].supplement.amount | menuOffers[0].parts[1].supplement.currencyCode | menuOffers[0].parts[1].options[0].name | menuOffers[0].parts[1].options[0].description      | menuOffers[0].parts[1].options[0].supplement.amount | menuOffers[0].parts[1].options[0].supplement.currencyCode | menuOffers[0].parts[1].options[0].required | menuOffers[0].parts[1].options[1].name | menuOffers[0].parts[1].options[1].description   | menuOffers[0].parts[1].options[1].supplement.amount | menuOffers[0].parts[1].options[1].supplement.currencyCode | menuOffers[0].parts[1].options[1].required | menuOffers[0].parts[2].name | menuOffers[0].parts[2].required | menuOffers[0].parts[2].supplement.amount | menuOffers[0].parts[2].supplement.currencyCode | menuOffers[0].parts[2].options[0].name | menuOffers[0].parts[2].options[0].description | menuOffers[0].parts[2].options[0].supplement.amount | menuOffers[0].parts[2].options[0].supplement.currencyCode | menuOffers[0].parts[2].options[0].required | menuOffers[0].parts[2].options[1].name | menuOffers[0].parts[2].options[1].description | menuOffers[0].parts[2].options[1].supplement.amount | menuOffers[0].parts[2].options[1].supplement.currencyCode | menuOffers[0].parts[2].options[1].required | menuOffers[0].recurringServingRanges[0].startTime.time | menuOffers[0].recurringServingRanges[0].startTime.day | menuOffers[0].recurringServingRanges[0].endTime.time | menuOffers[0].recurringServingRanges[0].endTime.day |
      | Lunch Monday Special | 39.99                          | PLN                                  | First Course                | true                            | 0.0                                      | PLN                                            | Tomato Soup                            | Classic tomato soup with basil and cream      | 0.0                                                 | PLN                                                       | false                                      | Tripe Soup                             | Traditional Polish tripe soup                 | 2.5                                                 | PLN                                                       | false                                      | Main Course                 | true                            | 0.0                                      | PLN                                            | Grilled Chicken Breast                 | Served with mashed potatoes and steamed vegetables | 0.0                                                 | PLN                                                       | false                                      | Pork Schnitzel                         | Breaded pork cutlet served with fries and salad | 3.0                                                 | PLN                                                       | false                                      | Dessert                     | false                           | 5.0                                      | PLN                                            | Apple Pie                              | Warm apple pie with vanilla ice cream         | 0.0                                                 | PLN                                                       | false                                      | Cheesecake                             | Creamy cheesecake with raspberry sauce        | 1.5                                                 | PLN                                                       | false                                      | 11:00:00                                               | MONDAY                                                | 14:00:00                                             | MONDAY                                              |



    And Refresh Hibernate Search indexes

    And response code is 200
    And User logs out (by removing saved auth token)

  Scenario Outline: Lunch serving time based places search

    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp


    When Place Search request is sent as below:
      | hasLunchServedAt | size | page |
      | <dayTime>        | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size <expectedResults> in path results

    Examples:
      | dayTime             | expectedResults | comment                                                                                 |
      | 2024-10-15T21:40:00 | 0               |                                                                                         |
      | 2024-10-21T10:00:00 | 1               |                                                                                         |
      | 2024-10-14T11:00:00 | 1               | # Poniedziałek, start serwowania lunchu (recurring)                                     |
      | 2024-10-14T14:00:00 | 0               | # Poniedziałek, koniec serwowania lunchu (recurring)                                    |
      | 2024-10-14T13:59:00 | 1               | # Poniedziałek, tuż przed końcem serwowania lunchu (recurring)                          |
      | 2024-10-15T11:30:00 | 1               | # Wtorek, w trakcie serwowania lunchu (recurring)                                       |
      | 2024-10-15T14:01:00 | 0               | # Wtorek, po zakończeniu serwowania lunchu (recurring)                                  |
      | 2024-10-13T11:00:00 | 1               | # Niedziela, początek                                                                   |
      | 2024-10-13T23:59:00 | 1               | # Niedziela, w trakcie serwowania lunchu (recurring, kończy się o 01:00 w poniedziałek) |
      | 2024-10-14T00:30:00 | 1               | # Poniedziałek 00:30, serwowanie lunchu (recurring z niedzieli do poniedziałku)         |
      | 2024-10-14T01:00:00 | 0               | # Poniedziałek, zakończenie niedzielnego lunchu o 01:00 (recurring)                     |
      | 2024-10-14T01:01:00 | 0               | # Poniedziałek, po zakończeniu niedzielnego lunchu o 01:00 (recurring)                  |
      | 2024-10-21T10:00:00 | 1               | # Jednorazowy lunch, 20 października od 10:00 (one-time)                                |
      | 2024-10-21T20:34:00 | 0               | # Jednorazowy lunch, koniec o 20:34 (one-time)                                          |
      | 2024-10-21T20:35:00 | 0               | # Jednorazowy lunch, koniec o 20:35 (one-time)                                          |
      | 2024-10-21T20:33:00 | 1               | # Jednorazowy lunch, tuż przed zakończeniem (one-time)                                  |
      | 2024-10-30T08:00:00 | 1               | # Jednorazowy lunch, 30 października od 08:00 (one-time)                                |
      | 2024-10-30T09:34:00 | 0               | # Jednorazowy lunch, na zakończenie o 09:34 (one-time)                                  |
      | 2024-10-30T09:35:00 | 0               | # Jednorazowy lunch, po zakończeniu o 09:34 (one-time)                                  |
      | 2024-11-15T22:55:00 | 1               | # Jednorazowy lunch, 15 listopada                                                       |
      | 2024-11-15T23:59:00 | 1               | # Jednorazowy lunch, 15 listopada                                                       |
      | 2024-11-16T00:00:00 | 1               | # Jednorazowy lunch, 15 listopada                                                       |
      | 2024-11-16T00:01:00 | 1               | # Jednorazowy lunch, 15 listopada                                                       |
      | 2024-11-16T22:56:00 | 0               | # Jednorazowy lunch, po zakończeniu o 22:56 (one-time)                                  |
