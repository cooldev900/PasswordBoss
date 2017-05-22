/* global module */
var TypeDefinitions = (function() {
  var typeDefinitions = {
    FieldTypes: {
      unknown: 'unknown',
      email: 'email',
      username: 'username',
      accountNumber: 'accountNumber',
      address1: 'address1',
      address2: 'address2',
      addressRef: 'addressRef',
      apt: 'apt',
      autologin: 'autologin',
      bank: 'bank',
      cardNumber: 'cardNumber',
      cardType: 'cardType',
      city: 'city',
      color: 'color',
      company: 'company',
      country: 'country',
      cvv: 'cvv',
      dateOfBirth: 'dateOfBirth',
      domain: 'domain', //not on list but used by the backend
      driverLicenceNumber: 'driverLicenceNumber',
      expiresDay: 'expiresDay',
      expiresMonth: 'expiresMonth',
      expiresYear: 'expiresYear',
      firstName: 'firstName',
      gender: 'gender',
      iban: 'iban',
      issueDate: 'issueDate',
      issuingBank: 'issuingBank',
      lastName: 'lastName',
      memberID: 'memberID',
      middleName: 'middleName',
      nameOnAccount: 'nameOnAccount',
      nameOnCard: 'nameOnCard',
      nationality: 'nationality',
      notes: 'notes',
      passportNumber: 'passportNumber',
      password: 'password',
      phoneNumber: 'phoneNumber',
      pin: 'pin',
      placeOfIssue: 'placeOfIssue',
      reEnterPassword: 'reEnterPassword',
      routingNumber: 'routingNumber',
      ssn: 'ssn',
      state: 'state',
      subDomain: 'subDomain',
      swift: 'swift',
      title: 'title',
      useSecureBrowser: 'useSecureBrowser',
      zipCode: 'zipCode',
      //composite
      fullName: 'fullName'
    },
    ButtonTypes: {
      signupButton: 'signupButton',
      loginButton: 'loginButton',
      unknownButton: 'unknownButton'
    },
    FormTypes: {
      unknownForm: 'unknownForm',
      signupForm: 'signupForm',
      loginForm: 'loginForm'
    },
    RealTypeUsage: {
      capture: 'capture',
      fill: 'fill'
    }
  };

  typeDefinitions.FieldTypes.expires = [
    typeDefinitions.FieldTypes.expiresDay, typeDefinitions.FieldTypes.expiresMonth, typeDefinitions.FieldTypes.expiresYear
  ];

  return typeDefinitions;
})();

if (typeof module !== 'undefined') {
  module.exports = TypeDefinitions;
}

/* global module */
var Utils = (function () {
  function normalizeSubmitData(submitData) {
    if (typeof submitData === 'string') {
      var formDataObject = {};

      submitData.split('&').forEach(function (property) {
        var t = property.split('=');
        if (formDataObject[t[0]] === undefined) {
          formDataObject[t[0]] = [];
        }
        formDataObject[decodeURIComponent(t[0])].push(decodeURIComponent(t[1]));
      });

      return formDataObject;
    }
    else {
      return submitData;
    }
  }

  function getCreditCardType(accountNumber) {
    //taken from: http://webstandardssherpa.com/reviews/auto-detecting-credit-card-type/

    //start without knowing the credit card type
    var result = 'Other';

    //first check for MasterCard
    if (/^5[1-5]/.test(accountNumber)) {
      result = 'MasterCard';
    }
    //then check for Visa
    else if (/^4/.test(accountNumber)) {
      result = 'Visa';
    }
    //then check for AmEx
    else if (/^3[47]/.test(accountNumber)) {
      result = 'American Express';
    }

    return result;
  }

  function deriveDate(date) {
    return {day: date.getUTCDate(), month: date.getUTCMonth() + 1, year: date.getUTCFullYear()};
  }

  function composeDate(day, month, year) {
    return day ?
      new Date(Date.UTC(year, month - 1, day, 23, 59, 59, 999)).toISOString() :
      new Date(Date.UTC(year, month, 1) - 1).toISOString();
  }

  function isEmailValid(email) {
    var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    return re.test(email);
  }

  return {
    normalizeSubmitData: normalizeSubmitData,
    getCreditCardType: getCreditCardType,
    deriveDate: deriveDate,
    composeDate: composeDate,
    isEmailValid: isEmailValid
  };
})();

if (typeof module !== 'undefined') {
  module.exports = Utils;
}

/* global module, require */
if (typeof require !== 'undefined') {
  var TypeDefinitions = require('./type-definitions');
}

var FormTypeDetector = (function (TypeDefinitions) {
  function hasSignupButtons(formRecord) {
    return formRecord.buttonRecords.some(function (br) {
      return br.buttonType === TypeDefinitions.ButtonTypes.signupButton;
    });
  }

  function hasLoginButtons(formRecord) {
    return formRecord.buttonRecords.some(function (br) {
      return br.buttonType === TypeDefinitions.ButtonTypes.loginButton;
    });
  }

  function isLoginForm(formRecord, multistep) {
    function notHaveExtraFields(formRecord) {
      var extraLoginFields = [
        TypeDefinitions.FieldTypes.firstName,
        TypeDefinitions.FieldTypes.lastName,
        TypeDefinitions.FieldTypes.reEnterPassword,
        TypeDefinitions.FieldTypes.fullName
      ];

      return formRecord.inputRecords.every(function (ir) {
        return extraLoginFields.indexOf(ir.fieldType) === -1;
      });
    }

    function hasAllLoginFields() {

      var hasPasswordField = formRecord.inputRecords.some(function (inputRecord) {
        return inputRecord.fieldType === TypeDefinitions.FieldTypes.password;
      });

      var hasEmailField = formRecord.inputRecords.some(function (inputRecord) {
        return inputRecord.fieldType === TypeDefinitions.FieldTypes.email;
      });

      var hasUsernameField = formRecord.inputRecords.some(function (inputRecord) {
        return inputRecord.fieldType === TypeDefinitions.FieldTypes.username;
      });

      //hasPasswordField && (hasEmailField XOR hasUsernameField)
      return hasPasswordField && hasEmailField !== hasUsernameField;
    }

    function hasSomeLoginFields() {
      var loginFields = [
        TypeDefinitions.FieldTypes.username,
        TypeDefinitions.FieldTypes.email,
        TypeDefinitions.FieldTypes.password
      ];

      return formRecord.inputRecords.some(function (inputRecord) {
        return loginFields.indexOf(inputRecord.fieldType) !== -1;
      });
    }

    return notHaveExtraFields(formRecord) && (hasAllLoginFields() || (multistep && hasSomeLoginFields()) || isProbablyMultiStep(formRecord));
  }

  //a form is probably a multi-step form if it only contains an username or password
  function isProbablyMultiStep(formRecord) {
    var fieldTypes = formRecord.inputRecords.filter(function (inputRecord) {
      return inputRecord.fieldType !== TypeDefinitions.FieldTypes.unknown;
    }).map(function (inputRecord) {
      return inputRecord.fieldType;
    });

    if (fieldTypes.length === 0) {
      return false;
    }

    var fieldTypesContainOnlyUsername = fieldTypes.every(function (fieldType) {
      return fieldType === TypeDefinitions.FieldTypes.username;
    });

    var fieldTypesContainOnlyPassword = fieldTypes.every(function (fieldType) {
      return fieldType === TypeDefinitions.FieldTypes.password;
    });

    return fieldTypesContainOnlyUsername || fieldTypesContainOnlyPassword;
  }

  function detectFormType(formRecord, multiStep) {
    if (hasSignupButtons(formRecord) && !(hasLoginButtons(formRecord))) {
      return TypeDefinitions.FormTypes.signupForm;
    }

    if (isLoginForm(formRecord, multiStep)) {
      return TypeDefinitions.FormTypes.loginForm;
    }

    return TypeDefinitions.FormTypes.unknownForm;
  }

  function findFormRecordFromSubmitData(pageRecord, submitData) {
    var likelyFormRecord = pageRecord.formRecords.map(function (formRecord) {
      return {
        formRecord: formRecord,
        rank: formRecord.inputRecords.filter(function (inputRecord) {
          return submitData[inputRecord.name] !== undefined;
        }).length
      };
    }).sort(function (a, b) {
      return b.rank - a.rank;
    });

    return likelyFormRecord.length > 0 ? likelyFormRecord[0].formRecord : undefined;
  }

  function getLoginFormRecord(pageRecord, multistep) {
    var formRecords = pageRecord.formRecords.filter(function (formRecord) {
      return FormTypeDetector.detectFormType(formRecord, multistep) === TypeDefinitions.FormTypes.loginForm;
    });

    return formRecords[0] || null;
  }

  function getFormRecordForInputPath(pageRecord, inputPath) {
    return pageRecord.formRecords.filter(function (formRecord) {
      return formRecord.inputRecords.some(function (inputRecord) {
        return inputRecord.path === inputPath;
      });
    })[0];
  }

  return {
    detectFormType: detectFormType,
    detectFormTypeJson: function (formRecord, multistep) {
      return detectFormType(JSON.parse(formRecord), multistep);
    },
    findFormRecordFromSubmitData: findFormRecordFromSubmitData,
    findLoginFormRecord: function (pageRecordJson, multistep) {
      return JSON.stringify(getLoginFormRecord(JSON.parse(pageRecordJson), multistep));
    },
    isProbablyMultiStep: function (formRecordJson) {
      return isProbablyMultiStep(JSON.parse(formRecordJson));
    },
    detectFormTypeForInputPath: function(pageRecordJson, isMultiStep, inputRecordPath) {
      var formRecord = getFormRecordForInputPath(JSON.parse(pageRecordJson), inputRecordPath);
      return detectFormType(formRecord, isMultiStep);
    }
  };
})(TypeDefinitions);

if (typeof module !== 'undefined') {
  module.exports = FormTypeDetector;
}

/* global module, require */
if (typeof require !== 'undefined') {
  var TypeDefinitions = require('./type-definitions');
  var PhoneUtils = require('./phone-utils');
}

var RealTypeFactory = (function (TypeDefinitions) {
  //if a parameter has the suffix 'JSON', that means it receives a reference type from the various backends and needs
  //to be parsed first. Parameters without a 'JSON' suffix are either value types, or are not meant for communication
  //with backends
  var realType = {
    discriminatorFieldTypes: [],
    isValueSame: function (first, second, key) {
      return first[key] !== undefined && second[key] !== undefined && first[key] === second[key];
    },
    isComposedOf: function (fieldTypes) {
      if (this.discriminatorFieldTypes.length === 0) {
        return this.fieldTypes.every(function (fieldType) {
          return fieldTypes.indexOf(fieldType) >= 0;
        });
      }

      return this.discriminatorFieldTypes.some(function (fieldType) {
        return fieldTypes.indexOf(fieldType) >= 0;
      }, this);
    },
    isDomainSpecific: false,
    captureFormTypes: [
      TypeDefinitions.FormTypes.loginForm,
      TypeDefinitions.FormTypes.signupForm,
      TypeDefinitions.FormTypes.unknownForm
    ],
    fillFormTypes: [
      TypeDefinitions.FormTypes.loginForm,
      TypeDefinitions.FormTypes.signupForm,
      TypeDefinitions.FormTypes.unknownForm
    ]
  };

  var personalInfo = Object.create(realType);
  personalInfo.type = 'PI';
  personalInfo.captureFormTypes = [
    TypeDefinitions.FormTypes.signupForm,
    TypeDefinitions.FormTypes.unknownForm
  ];
  personalInfo.fillFormTypes = [
    TypeDefinitions.FormTypes.signupForm,
    TypeDefinitions.FormTypes.unknownForm
  ];

  var digitalWallet = Object.create(realType);
  digitalWallet.type = 'DW';
  digitalWallet.captureFormTypes = [
    TypeDefinitions.FormTypes.signupForm,
    TypeDefinitions.FormTypes.unknownForm
  ];
  digitalWallet.fillFormTypes = [
    TypeDefinitions.FormTypes.signupForm,
    TypeDefinitions.FormTypes.unknownForm
  ];

  //#region Names
  var names = Object.create(personalInfo);
  names.subType = 'Names';
  names.fieldTypes = [
    TypeDefinitions.FieldTypes.firstName,
    TypeDefinitions.FieldTypes.middleName,
    TypeDefinitions.FieldTypes.lastName,
    TypeDefinitions.FieldTypes.fullName
  ];
  names.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.firstName,
    TypeDefinitions.FieldTypes.middleName,
    TypeDefinitions.FieldTypes.lastName,
    TypeDefinitions.FieldTypes.fullName
  ];
  names.getCaption = function (dataJSON) {
    var data = JSON.parse(dataJSON);
    return data.firstName + ' ' + data.lastName;
  };
  names.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.firstName) &&
      this.isValueSame(first, second, TypeDefinitions.FieldTypes.lastName) ||
      this.isValueSame(first, second, TypeDefinitions.FieldTypes.fullName);
  };
  names.isComposedOf = function (fieldTypes) {
    return (fieldTypes.indexOf(TypeDefinitions.FieldTypes.firstName) >= 0 && fieldTypes.indexOf(TypeDefinitions.FieldTypes.lastName) >= 0) ||
      fieldTypes.indexOf(TypeDefinitions.FieldTypes.fullName) >= 0;
  };
  //#endregion

  //#region Address
  var address = Object.create(personalInfo);
  address.subType = 'Address';
  address.fieldTypes = [
    TypeDefinitions.FieldTypes.address1,
    TypeDefinitions.FieldTypes.address2,
    TypeDefinitions.FieldTypes.city,
    TypeDefinitions.FieldTypes.country,
    TypeDefinitions.FieldTypes.state,
    TypeDefinitions.FieldTypes.zipCode
  ];
  address.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.address1,
    TypeDefinitions.FieldTypes.address2,
    TypeDefinitions.FieldTypes.zipCode
  ];
  address.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).address1;
  };
  address.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.address1);
  };
  //#endregion

  //#region Company
  var company = Object.create(personalInfo);
  company.subType = 'Company';
  company.fieldTypes = [
    TypeDefinitions.FieldTypes.company
  ];
  company.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.company
  ];
  company.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).company;
  };
  company.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.company);
  };
  //#endregion

  //#region DriversLicence
  var driversLicence = Object.create(personalInfo);
  driversLicence.subType = 'DriverLicense';
  driversLicence.fieldTypes = [
    TypeDefinitions.FieldTypes.firstName,
    TypeDefinitions.FieldTypes.lastName,
    TypeDefinitions.FieldTypes.country,
    TypeDefinitions.FieldTypes.state,
    TypeDefinitions.FieldTypes.driverLicenceNumber,
    TypeDefinitions.FieldTypes.expires,
    TypeDefinitions.FieldTypes.placeOfIssue
  ];
  driversLicence.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.driverLicenceNumber
  ];
  driversLicence.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).driverLicenceNumber;
  };
  driversLicence.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.driverLicenceNumber);
  };
  //#endregion

  //#region Email
  var email = Object.create(personalInfo);
  email.subType = 'Email';
  email.fieldTypes = [
    TypeDefinitions.FieldTypes.email
  ];
  email.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.email
  ];
  email.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).email;
  };
  email.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.email);
  };
  email.captureFormTypes = [
    TypeDefinitions.FormTypes.signupForm,
    TypeDefinitions.FormTypes.unknownForm,
    TypeDefinitions.FormTypes.loginForm
  ];
  email.fillFormTypes = [
    TypeDefinitions.FormTypes.signupForm,
    TypeDefinitions.FormTypes.unknownForm,
    TypeDefinitions.FormTypes.loginForm
  ];
  //#endregion

  //#region MemberId
  var memberId = Object.create(personalInfo);
  memberId.subType = 'MemberIDs';
  memberId.fieldTypes = [
    TypeDefinitions.FieldTypes.firstName,
    TypeDefinitions.FieldTypes.lastName,
    TypeDefinitions.FieldTypes.memberID
  ];
  memberId.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.memberID
  ];
  memberId.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).memberID;
  };
  memberId.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.memberID);
  };
  //#endregion

  //#region Passport
  var passport = Object.create(personalInfo);
  passport.subType = 'Passport';
  passport.fieldTypes = [
    TypeDefinitions.FieldTypes.firstName,
    TypeDefinitions.FieldTypes.lastName,
    TypeDefinitions.FieldTypes.nationality,
    TypeDefinitions.FieldTypes.dateOfBirth,
    TypeDefinitions.FieldTypes.passportNumber,
    TypeDefinitions.FieldTypes.issueDate,
    TypeDefinitions.FieldTypes.expires,
    TypeDefinitions.FieldTypes.placeOfIssue
  ];
  passport.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.passportNumber
  ];
  passport.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).passportNumber;
  };
  passport.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.passportNumber);
  };
  //#endregion

  //#region Phone
  var phone = Object.create(personalInfo);
  phone.subType = 'PhoneNumber';
  phone.fieldTypes = [
    TypeDefinitions.FieldTypes.phoneNumber
  ];
  phone.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.phoneNumber
  ];
  phone.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).phoneNumber;
  };
  phone.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.phoneNumber);
    //TODO: this should be used after the PhoneUtils library passes compilation
    //return PhoneUtils.isNumberMatch(first[TypeDefinitions.FieldTypes.phoneNumber], second[TypeDefinitions.FieldTypes.phoneNumber]) > 2;
  };
  //#endregion

  //#region SocialSecurity
  var socialSecurity = Object.create(personalInfo);
  socialSecurity.subType = 'SocialSecurity';
  socialSecurity.fieldTypes = [
    TypeDefinitions.FieldTypes.firstName,
    TypeDefinitions.FieldTypes.lastName,
    TypeDefinitions.FieldTypes.dateOfBirth,
    TypeDefinitions.FieldTypes.ssn
  ];
  socialSecurity.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.ssn
  ];
  socialSecurity.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).ssn;
  };
  socialSecurity.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.ssn);
  };
  //#endregion

  //#region BankAccount
  var bankAccount = Object.create(digitalWallet);
  bankAccount.subType = 'Bank';
  bankAccount.fieldTypes = [
    TypeDefinitions.FieldTypes.country,
    TypeDefinitions.FieldTypes.bank,
    TypeDefinitions.FieldTypes.nameOnAccount,
    TypeDefinitions.FieldTypes.swift,
    TypeDefinitions.FieldTypes.iban,
    TypeDefinitions.FieldTypes.routingNumber,
    TypeDefinitions.FieldTypes.accountNumber
  ];
  bankAccount.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.bank
  ];
  bankAccount.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).bank;
  };
  //TODO: implement compare
  //#endregion

  //#region CreditCard
  var creditCard = Object.create(digitalWallet);
  creditCard.subType = 'CreditCard';
  creditCard.fieldTypes = [
    TypeDefinitions.FieldTypes.cardNumber,
    TypeDefinitions.FieldTypes.cardType,
    TypeDefinitions.FieldTypes.nameOnCard,
    TypeDefinitions.FieldTypes.placeOfIssue,
    TypeDefinitions.FieldTypes.cvv,
    TypeDefinitions.FieldTypes.pin,
    TypeDefinitions.FieldTypes.expires
  ];
  creditCard.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.cardNumber
  ];
  creditCard.getCaption = function (dataJSON) {
    var data = JSON.parse(dataJSON);

    return (data.cardType || 'Other') + ' *' + data.cardNumber.slice(-4);
  };
  creditCard.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.cardNumber);
  };
  //#endregion

  //#region PasswordVault
  var passwordVault = Object.create(realType);
  passwordVault.type = 'PV';
  passwordVault.subType = 'PV';
  passwordVault.fieldTypes = [
    TypeDefinitions.FieldTypes.username,
    TypeDefinitions.FieldTypes.email,
    TypeDefinitions.FieldTypes.password,
    TypeDefinitions.FieldTypes.domain
  ];
  passwordVault.discriminatorFieldTypes = [
    TypeDefinitions.FieldTypes.password,
    TypeDefinitions.FieldTypes.email,
    TypeDefinitions.FieldTypes.username
  ];
  passwordVault.getCaption = function (dataJSON) {
    return JSON.parse(dataJSON).username || '';
  };
  passwordVault.compare = function (firstJSON, secondJSON) {
    var first = JSON.parse(firstJSON), second = JSON.parse(secondJSON);

    return this.isValueSame(first, second, TypeDefinitions.FieldTypes.username) ||
      this.isValueSame(first, second, TypeDefinitions.FieldTypes.email);
  };
  passwordVault.isDomainSpecific = true;
  passwordVault.fillFormTypes = [
    TypeDefinitions.FormTypes.loginForm
  ];
  passwordVault.isComposedOf = function (fieldTypes, isMultiStepForm) {
    var fieldTypesHavePassword = fieldTypes.indexOf(TypeDefinitions.FieldTypes.password) >= 0;
    var fieldTypesHaveUsername = fieldTypes.indexOf(TypeDefinitions.FieldTypes.username) >= 0;
    var fieldTypesHaveEmail = fieldTypes.indexOf(TypeDefinitions.FieldTypes.email) >= 0;

    return isMultiStepForm ? fieldTypesHaveUsername || fieldTypesHaveEmail || fieldTypesHavePassword : fieldTypesHavePassword && (fieldTypesHaveUsername || fieldTypesHaveEmail);
  };
  //#endregion

  var realTypes = [passwordVault, names, address, company, driversLicence, email, memberId, passport, phone, socialSecurity, bankAccount, creditCard];

  return {
    createRealType: function (name) {
      return realTypes.filter(function (realType) {
        return name === realType.subType;
      })[0];
    },
    getAllRealTypes: function () {
      return realTypes;
    }
  };
})(TypeDefinitions, PhoneUtils);

if (typeof module !== 'undefined') {
  module.exports = RealTypeFactory;
}

/* global module, require */
if (typeof require !== 'undefined') {
  var TypeDefinitions = require('./type-definitions');
}

function Node(path, allInputRecords) {
  this.path = path;
  this.inputRecords = allInputRecords.filter(function (inputRecord) {
    return inputRecord.path.indexOf(this.path) === 0;
  }, this);
  this.allInputRecords = allInputRecords;
}

Node.prototype.parent = function () {
  return (this.path === '') ? null : new Node(this.path.split('>').slice(0, -1).join('>'), this.allInputRecords);
};

Node.prototype.findConflicts = function (fieldTypes) {
  var valuesSoFar = {}, conflicts = [];

  this.inputRecords.forEach(function (inputRecord) {
    if (fieldTypes.indexOf(inputRecord.fieldType) >= 0 && Object.prototype.hasOwnProperty.call(valuesSoFar, inputRecord.fieldType)) {
      conflicts.push(inputRecord);
    }

    if (inputRecord.fieldType !== TypeDefinitions.FieldTypes.unknown) {
      valuesSoFar[inputRecord.fieldType] = true;
    }
  });

  return conflicts;
};

Node.prototype.hasConflicts = function (fieldTypes) {
  return this.findConflicts(fieldTypes).length > 0;
};

if (typeof module !== 'undefined') {
  module.exports = Node;
}
/* global module, require */
if (typeof require !== 'undefined') {
  var Utils = require('./utils');
  var TypeDefinitions = require('./type-definitions');
  var RealTypeFactory = require('./real-type-factory');
  var FormTypeDetector = require('./form-type-detector');
  var Node = require('./node'); // jshint ignore:line
}

var RealTypeDetector = (function (realTypes, TypeDefinitions, FormTypeDetector, Utils, Node) {

  function getRealTypesForInputRecords(inputRecords, path, usage, formType, isMultiStep) {
    var fieldTypes = inputRecords.filter(function(inputRecord) {
      return inputRecord.fieldType !== TypeDefinitions.FieldTypes.unknown;
    }).map(function (inputRecord) {
      return inputRecord.fieldType;
    });

    if (fieldTypes.length === 0) {
      return [];
    }

    var fieldTypesContainUsername = fieldTypes.some(function (fieldType) {
      return fieldType === TypeDefinitions.FieldTypes.username;
    });

    //The isMultiStep param is coming from the backend and is true if the user has logged in and we flagged the site as multi-step, however
    //if it is false we can mark it as probably a multi-step website if the fields contain only the username on the first step,
    //and only the password on the second step. This is important because otherwise the system will not generate a PV item
    var fieldTypesContainOnlyUsername = fieldTypes.every(function (fieldType) {
      return fieldType === TypeDefinitions.FieldTypes.username;
    });
    var fieldTypesContainOnlyPassword = fieldTypes.every(function (fieldType) {
      return fieldType === TypeDefinitions.FieldTypes.password;
    });
    var probablyMultiStep = fieldTypesContainOnlyUsername || fieldTypesContainOnlyPassword;

    return realTypes.filter(function (rt) {
      return usage !== TypeDefinitions.RealTypeUsage.fill || rt.fillFormTypes.indexOf(formType) >= 0;
    }).filter(function (rt) {
      return usage !== TypeDefinitions.RealTypeUsage.capture || rt.captureFormTypes.indexOf(formType) >= 0;
    }).filter(function (rt) {
      //when capturing forms multi step is always false because it will never have a full PV item, it will only have a password
      return rt.isComposedOf(fieldTypes, usage !== TypeDefinitions.RealTypeUsage.capture && (isMultiStep || probablyMultiStep));
    }).map(function (rt) {
      //TODO: simplify
      return {
        name: rt.subType,
        isDomainSpecific: !!rt.isDomainSpecific,
        inputRecords: inputRecords.filter(function (inputRecord) {
          return rt.fieldTypes.some(function (fieldType) {
            if (rt.type === 'PV' && fieldType === TypeDefinitions.FieldTypes.email && fieldTypesContainUsername) {
              return false;
            }
            else if (typeof fieldType === 'object') {
              return fieldType.indexOf(inputRecord.fieldType) >= 0;
            }
            else {
              return fieldType === inputRecord.fieldType;
            }
          });
        }).map(function (inputRecord) {
          var ir = JSON.parse(JSON.stringify(inputRecord));

          if (rt.type === 'PV' && ir.fieldType === TypeDefinitions.FieldTypes.email) {
            ir.fieldType = TypeDefinitions.FieldTypes.username;
          }

          return ir;
        }),
        path: path
      };
    });
  }

  function extractRealTypesDfs(formRecord, usage, isMultiStepForm) {
    var realTypesExtracted = [], node, inputRecordsForRealTypes, eligibleRealTypes, path = formRecord.path, inputRecords = formRecord.inputRecords;
    var conflicts = new Node(path, inputRecords).findConflicts(Object.keys(TypeDefinitions.FieldTypes));
    var formType = FormTypeDetector.detectFormType(formRecord, isMultiStepForm);

    while (conflicts.length > 0) {
      node = new Node(conflicts[0].path, inputRecords);
      while (node.parent() !== null && node.parent().hasConflicts([conflicts[0].fieldType]) === false) {
        node = node.parent();
      }

      eligibleRealTypes = realTypes.filter(function (realType) {
        return realType.discriminatorFieldTypes.indexOf(conflicts[0].fieldType) >= 0;
      }); // jshint ignore:line

      //in all probability this is an error state, happens for instance for 2 expires on petsonline, which is
      //happening because the keywords haven't been updated
      if (eligibleRealTypes.length === 0) {
        inputRecords = inputRecords.filter(function (inputRecord) {
          return conflicts[0] !== inputRecord;
        }); // jshint ignore:line

        conflicts.splice(0, 1);
      }
      else {
        inputRecordsForRealTypes = node.inputRecords.filter(function (inputRecord) {
          return eligibleRealTypes.some(function (realType) {
            return realType.fieldTypes.indexOf(inputRecord.fieldType) >= 0;
          });
        }); // jshint ignore:line

        getRealTypesForInputRecords(inputRecordsForRealTypes, node.path, usage, formType, isMultiStepForm).forEach(function (realType) {
          realTypesExtracted.push(realType);
        }); // jshint ignore:line

        //remove resolved conflicts
        conflicts = conflicts.filter(function (inputRecord) {
          return inputRecordsForRealTypes.indexOf(inputRecord) < 0;
        }); // jshint ignore:line

        inputRecords = inputRecords.filter(function (inputRecord) {
          return inputRecordsForRealTypes.indexOf(inputRecord) < 0;
        }); // jshint ignore:line
      }
    }

    getRealTypesForInputRecords(inputRecords, formRecord.path, usage, formType, isMultiStepForm).forEach(function (realType) {
      realTypesExtracted.push(realType);
    });

    return realTypesExtracted;
  }

  //used on form submit, before saving/editing the item
  function extractRealTypesWithData(formRecord, isMultiStep) {
    var realTypesWithData = [];

    //filter out all input records without value
    formRecord.inputRecords = formRecord.inputRecords.filter(function (inputRecord) {
      return inputRecord.value.trim().length > 0;
    });

    extractRealTypesDfs(formRecord, TypeDefinitions.RealTypeUsage.capture, isMultiStep).forEach(function (realType) {
      var data = {};

      realType.inputRecords.forEach(function (inputRecord) {
        data[inputRecord.fieldType] = inputRecord.value;
      });

      if (realType.name === 'CreditCard') {
        data[TypeDefinitions.FieldTypes.cardType] = Utils.getCreditCardType(data[TypeDefinitions.FieldTypes.cardNumber]);
      }

      if(realType.name === 'Email' && !Utils.isEmailValid(data[TypeDefinitions.FieldTypes.email])) {
        return;
      }

      //convert full name to firstName, lastName
      deriveName(data);
      //convert day, month, year fields to full date
      //FIXME: hardcoded
      composeDate('expires', data);
      composeDate('dateOfBirth', data);
      composeDate('issueDate', data);

      realTypesWithData.push({realType: realType.name, data: data});
    });

    return realTypesWithData;
  }

  function deriveDate(dateFieldName, data) {
    if (data[dateFieldName]) {
      var date = Utils.deriveDate(new Date(data[dateFieldName]));
      data[dateFieldName + 'Day'] = date.day;
      data[dateFieldName + 'Month'] = date.month;
      data[dateFieldName + 'Year'] = date.year;

      delete data[dateFieldName];
    }
  }
  
  function composeDate(dateFieldName, data) {
    var yearField = data[dateFieldName + 'Year'];
    var monthField = data[dateFieldName + 'Month'];
    var dayField = data[dateFieldName + 'Day'];

    if (monthField && yearField) {
      data[dateFieldName] = Utils.composeDate(dayField ? dayField : undefined, monthField, yearField);
    }
  }

  function deriveName(data) {
    var fullName = data[TypeDefinitions.FieldTypes.fullName];
    if (fullName) {
      var name = fullName.split(' ');

      data[TypeDefinitions.FieldTypes.firstName] = name[0];
      data[TypeDefinitions.FieldTypes.lastName] = name[1];

      delete data[TypeDefinitions.FieldTypes.fullName];
    }
  }

  function composeName(data) {
    data[TypeDefinitions.FieldTypes.fullName] = data.firstName + ' ' + data.lastName;
  }

  function extractRealTypeWithInputRecord(formRecord, usage, isMultiStep, selectedInputRecord) {
    var realTypes = extractRealTypesDfs(formRecord, usage, isMultiStep);

    return realTypes.filter(function (realType) {
      return realType.inputRecords.some(function (inputRecord) {
        return inputRecord.path === selectedInputRecord;
      });
    });
  }

  function generateFillDataMessageData(formRecord, isMultiStep, secureItemData, secureItemType, selectedInputRecordPath) {
    var realTypes = extractRealTypesDfs(formRecord, TypeDefinitions.RealTypeUsage.fill, isMultiStep), data = [];

    //add a fullName field if needed
    composeName(secureItemData);
    //convert dates to day, month, year fields
    //FIXME: hardcoded
    deriveDate('expires', secureItemData);
    deriveDate('dateOfBirth', secureItemData);
    deriveDate('issueDate', secureItemData);

    realTypes.filter(function (realType) {
      return realType.name === secureItemType;
    }).forEach(function (realType) {
      realType.inputRecords.forEach(function (inputRecord) {
        data.push({
          fieldPath: inputRecord.path,
          fieldValue: secureItemData[inputRecord.fieldType] ? secureItemData[inputRecord.fieldType] : '',
          alternativeValues: [],
          overwriteExistingValue: selectedInputRecordPath === '*' || selectedInputRecordPath.indexOf(realType.path) === 0
        });
      });
    });

    return data;
  }

  function generatePasswordGeneratorMessageData(formRecord, password) {
    var data = [];

    formRecord.inputRecords.filter(function(inputRecord) {
      return inputRecord.fieldType === TypeDefinitions.FieldTypes.password || inputRecord.fieldType === TypeDefinitions.FieldTypes.reEnterPassword;
    }).forEach(function(inputRecord) {
      data.push({
        fieldPath: inputRecord.path,
        fieldValue: password,
        alternativeValues: [],
        overwriteExistingValue: true
      });
    });

    return data;
  }

  function getFormRecordForInputPath(pageRecord, inputPath) {
    return pageRecord.formRecords.filter(function (formRecord) {
      return formRecord.inputRecords.some(function (inputRecord) {
        return inputRecord.path === inputPath;
      });
    })[0];
  }

  //adapter methods suitable for all platforms, every complex type is passed in as JSON, and every output is JSON
  return {
    //used on android
    extractFromPageRecord: function (pageRecordJson, isMultiStep, selectedInputPath) {
      var formRecord = getFormRecordForInputPath(JSON.parse(pageRecordJson), selectedInputPath);
      var realTypes = extractRealTypesDfs(formRecord, 'fill', isMultiStep);

      //remove PV if the form is multistep and the only field in PV is passwprd
      realTypes = realTypes.filter(function (realType) {
        var hasUsernameOrEmail = realType.inputRecords.some(function (inputRecord) {
          return inputRecord.fieldType === TypeDefinitions.FieldTypes.username || inputRecord.fieldType === TypeDefinitions.FieldTypes.email;
        });

        return !isMultiStep || realType.name !== 'PV' || hasUsernameOrEmail;
      });

      //mark selected real type
      realTypes = realTypes.map(function (realType) {
        realType.selected = realType.inputRecords.some(function (inputRecord) {
          return inputRecord.path === selectedInputPath;
        });

        return realType;
      });

      return JSON.stringify(realTypes);
    },
    getRealTypeDataForSubmit: function (formRecordJson, isMultiStep) {
      var formRecord = JSON.parse(formRecordJson);
      return JSON.stringify(extractRealTypesWithData(formRecord, isMultiStep));
    },
    //used on desktop
    generateFillDataMessage: function (formRecordJson, isMultiStep, secureItemData, secureItemType, inputRecordPath) {
      return JSON.stringify(generateFillDataMessageData(JSON.parse(formRecordJson), isMultiStep, JSON.parse(secureItemData), secureItemType, inputRecordPath));
    },
    //used on android - bojan is lazy hence all the params
    generateFillDataMessageForPageRecord: function (pageRecordJson, isMultiStep, secureItemData, secureItemType, inputRecordPath, executeLogin, contentId, tabId) {
      var formRecord = getFormRecordForInputPath(JSON.parse(pageRecordJson), inputRecordPath);
      return JSON.stringify({
        formPath: formRecord.path,
        data: generateFillDataMessageData(formRecord, isMultiStep, JSON.parse(secureItemData), secureItemType, inputRecordPath),
        executeLogin: executeLogin === 'true',
        message: 'fillDataBegin',
        contentId: contentId,
        tabId: tabId
      });
    },
    generateFillDataMessageForOneClickLogin: function (pageRecordJson, isMultiStep, secureItemData, contentId, tabId) {
      var formRecord = JSON.parse(FormTypeDetector.findLoginFormRecord(pageRecordJson, isMultiStep));

      return JSON.stringify({
        formPath: formRecord.path,
        data: generateFillDataMessageData(formRecord, isMultiStep, JSON.parse(secureItemData), 'PV', '*'),
        executeLogin: true,
        message: 'fillDataBegin',
        contentId: contentId,
        tabId: tabId
      });
    },
    getFormRecordForInputPath: function (pageRecordJson, inputPath) {
      return JSON.stringify(getFormRecordForInputPath(JSON.parse(pageRecordJson), inputPath));
    },
    //used on iOS
    getRealTypeForInputPath: function (pageRecordJson, inputPath, isMultiStep) {
      var formRecord = getFormRecordForInputPath(JSON.parse(pageRecordJson), inputPath);
      return JSON.stringify(extractRealTypeWithInputRecord(formRecord, 'fill', isMultiStep, inputPath));
    },
    generatePasswordGeneratorMessage: function(pageRecordJson, password, inputRecordPath, contentId, tabId) {
      var formRecord = getFormRecordForInputPath(JSON.parse(pageRecordJson), inputRecordPath);
      return JSON.stringify({
        formPath: formRecord.path,
        data: generatePasswordGeneratorMessageData(formRecord, password),
        executeLogin: false,
        message: 'fillDataBegin',
        contentId: contentId,
        tabId: tabId
      });
    }
  };
})(RealTypeFactory.getAllRealTypes(), TypeDefinitions, FormTypeDetector, Utils, Node);

if (typeof module !== 'undefined') {
  module.exports = RealTypeDetector;
}
