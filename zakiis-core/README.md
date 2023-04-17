## zakiis-core

Core Module provides util class that we used frequently and exception structure.

### Exception Hierarchy

We have multi type of exception to distinguish different scenario:

- ZakiisThrowable - all exception derived from this interface, programs can catch this class to catch all exception from zakiis framework.
- ZakiisError - represents exceptions that is a disaster to current program and can't be recovered 
- ZakiisException - represents exceptions that should be caught and processed by caller, which named checked exception as we known.
- ZakiisRuntimeException - represents exceptions that is free for caller.

```java
ZakiisThrowable
|- ZakiisError
|- |- XxxError
|- ZakiisException
|- |- XxxCheckedException
|- |- ZakiisRuntimeException
|- |- |- XxxRuntimeException
```

Checked exception is disputed as it should be caught by caller, when there are too much this type of exception, caller may just catch it and do nothing in many times, exception is buried and cause the problem is difficult to locate.

```java
try {
	code
} catch (Throwable) {
    //do nothing
}
```

There is a point of view that we  should throw checked exception as few as possible. It's a good way to throw unchecked exception so that caller can choose the exception to process that they are able to  and throw other to processed  by top class.

### CaptchaUtil

captcha util provides the ability to generate image code. It has four parameters:

- width - the width of generated image code
- height - the height of generated image code
- captchaLength - the image code length, defaults to 4
- OutputStrem -  where the captcha image stream should write.

```java
FileOutputStream fos = new FileOutputStream(new File("target/captcha.png"));
CaptchaUtil.genImgCode(120, 40, 0, fos);
```

### ContextUtil

provides get, put, clear method to manipulate data on current thread scope.

### CsvUtil

You can use this class to save object to csv file and read object from csv file. As csv specification defined, field is separated by comma `,` , and  to avoid the scenario that the value contains comma, we use double quotation marks to package it. and in the case of the value has quotation, we use double quotation to represents on quotation. For example, if the values is `{"age": 56}`, we converted it to  `"{""age"":56}"`.

```java
@Test
public void testWrite() throws ParseException, FileNotFoundException {
    User user = new User();
    user.setName("zhang\"san, Li");
    user.setBirthDate(new Date());
    user.setAge(23);

    User user2 = new User();
    user2.setName("Li si");
    user2.setBirthDate(new Date());
    user2.setAge(25);

    User user3 = new User();
    user3.setName("Jack");
    user3.setBirthDate(new Date());

    List<User> users = Arrays.asList(user, user2, user3);
    CsvUtil.write(new FileOutputStream(new File("target/user.csv")), users, "utf-8");
}

@Test
public void testRead() throws FileNotFoundException {
    List<User> users = CsvUtil.read(new FileInputStream(new File("target/user.csv")), "utf-8", User.class);
    System.out.println(JsonUtil.toJson(users));
}
```

### JsonUtil

provide JSON serialize/deserialize ability.

```java
// list to json
List<User> userList = new ArrayList<User>();
userList.add(user);
user2.setName("Li Si");
userList.add(user2);
String content2 = JsonUtil.toJson(userList);

//json to list
List<User> userList2 = JsonUtil.toObject(content2, new TypeReference<List<User>>() {});
System.out.println(userList2);
```

### SecretFieldTokenizerUtil

tokenizing the secret field, separator tokens by 4 half-width character or 2 full-width character.

```java
List<String> phoeTokens = SecretFieldTokenizerUtil.tokens("186 1234 5678");
//[1861, 2345, 8612, 3456, 6123, 4567, 1234, 5678]
System.out.println(phoeTokens);

List<String> phoneSimpleTokens = SecretFieldTokenizerUtil.simpleTokens("1861234");
//[1861]
System.out.println(phoneSimpleTokens);
List<String> idSimpleTokens = SecretFieldTokenizerUtil.simpleTokens("19751024");
//[1975, 1024]
System.out.println(idSimpleTokens);
```

### SimilarityUtil

comparing the similarity of two strings.  The formula is `similarity = (1 - minDiffStep / maxStringLength) * 100`

```
//100
int similarity = SimilarityUtil.compare("Hello", "Hello");
//80
similarity = SimilarityUtil.compare("Hello", "Hallo");
//40
similarity = SimilarityUtil.compare("Hello", "eo");
```

### SnowFlakeUtil

Snow flake is an open distributed id generator created by facebook. It uses a Long type to represent a none repeat number, the structure is consisted by 5 part:

```
# part 1: 1 bit, always be zero, represent a positive number.
# part 2: 41 bit, represents current timestamp
# part 3: 5 bit, represents data center id
# part 4: 5 bit, represents the worker machine id
# part 5: 12 bit, represents an auto increment number
0 - 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0 - 00000 - 00000 - 0000 0000 0000
```

We need specify the data center id and worker machine id before generating id.

```
SnowFlakeUtil.init(2, 1);
long id = SnowFlakeUtil.generate();
```

