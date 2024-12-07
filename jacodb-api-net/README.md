# Usage

Run `Main.kt` with following cmd options: `--exe <exePath> --asms  <asm1.dll> <asm2.dll>`, where `<exePath>` is an
absolute path to TACBuilder executable, `<asmN.dll>` is an absolute path to assembly file from publication
[//]: # (## Approximations)

[//]: # ()

[//]: # (On generic parameter naming:)

[//]: # ()

[//]: # (- no namespace name)

[//]: # (- may be same for List<T> and Iterable<T> &#40;tokens are different, constraints may be different&#41;)

[//]: # (    * hence T may have specific methods)

[//]: # ()

[//]: # (```csharp)

[//]: # ([Approximation&#40;"System.Collecitons.Generic.List[T]"&#41;])

[//]: # (class ListApproximation {)

[//]: # (    // impl)

[//]: # (})

[//]: # (```)

[//]: # ()

[//]: # (Here we either use fully qualified name or reflection type &#40;that will be further converted to a pair of asm and type)

[//]: # (name&#41;)

[//]: # ()

[//]: # (Later, we may want to use the approximation for both `List<int>` and `List<string>` => we cannot rely on type fullname)

[//]: # (to find its approximation type)

[//]: # ()

[//]: # (```kotlin )

[//]: # (data class TypeId&#40;val asmName: String, val typeName: String, val genericArgs: List<TypeId>&#41;)

[//]: # (// here typeName == namespace.enclosingType+type)

[//]: # (// asmName is needed for situtations, where same typeNames found)

[//]: # (```)

[//]: # ()

[//]: # (`fun IlPublicationImpl.findIlTypeOrNull&#40;val id: TypeId&#41;: IlType?` use TypeId properties in order typeName ->)

[//]: # (genericArgs -> asmName &#40;latter may not be present&#41;)

[//]: # ()

[//]: # (Then it is possible to find approximated type by name only and substitute generic args)

[//]: # ()

[//]: # (# Approximations work on top of InctanceDto or Instance itself?)

[//]: # ()

[//]: # (# Left `Type<T>` as a fullname and add filtering constraints into `findType`)

[//]: # ()

[//]: # (# Stack overflow on typeId generation?)

[//]: # ()

[//]: # (# Backing field for generic parameter with metadata token?)

[//]: # ()

[//]: # (### Example)

[//]: # ()

[//]: # (```csharp)

[//]: # ([Approximation&#40;"Dictionary<K, V>"&#41;])

[//]: # (class DictionaryApproximation {)

[//]: # (    // impl)

[//]: # (})

[//]: # ([Approximaiton&#40;"List<T>"&#41;])

[//]: # (class ListApproximation {)

[//]: # (    // impl)

[//]: # (})

[//]: # ()

[//]: # (class UnderApprox {)

[//]: # (    Dictionary<string, List<int>> Field;)

[//]: # (    // impl)

[//]: # (})

[//]: # ()

[//]: # (```)

[//]: # (![virtual instances]&#40;./virtual-instances.png&#41;)