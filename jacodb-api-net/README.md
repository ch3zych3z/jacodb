# Usage

Run `Main.kt` with following cmd options: `--exe <exePath> --asms  <asm1.dll> <asm2.dll>`, where `<exePath>` is an
absolute path to TACBuilder executable, `<asmN.dll>` is an absolute path to assembly file from publication


## InMemoryHierarchy notes 

Suppose, there is a following hierarchy: 

```C#
class ArrayListString: List<string> ;

class ArrayListInt: List<int> ;

class ArrayList<T>: List<T>;
```

So after request for `List<string>` we expect it to have `ArrayListString and ArrayList<string>`

As it was discussed, first we have to find children of `List<T>`, these are `ArrayListString, ArrayListInt, ArrayList<T>`

Then, we want to check (improper situation is informative) `List<int> <: ArrayListString`

1. We may check exact request type occurrences in target type parents - this will fail due to `List<int> != List<string>`
    * A little more tricky approach here is to find same generic defn (asmName + typeName) and try to substitute argument into result
    * Problem may come for multiple generic args: 
2. 
