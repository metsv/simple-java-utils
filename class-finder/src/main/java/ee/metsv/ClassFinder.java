package ee.metsv;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
/*

Otsimise muster peab sisaldama CamelCase'is kirjutatud klassinimede
suuri tähti õiges järjekorras, mille vahel võivad olla ka järgnevad
väikesed tähed otsingutulemuste täpsustuseks, näiteks: "FB", "FoBa" ja
"FBar" peavad kõik leidma klasse "a.b.FooBarBaz" ja "c.d.FooBar".

Kui muster lõppeb tühikuga, siis mustri viimane sõna peab olema ka
viimaseks sõnaks leitud klassinimes, näiteks "FBar " leiab "FooBar"i,
aga mitte "FooBarBaz"i.

Mustris võivad esineda ka tärnid (*), mis matchivad suvalisi sõnu
algandmetes, näiteks " mõlemad leiavad "FooBarBaz"i.

Otsingu tulemus peab olema sorteeritud tähestikulises järjekorras
klassi nimede järgi (ilma package nimedeta).

Lahenduses ei tohi kasutada regexp'i. Unit Testid peavad ka olemas olema.

Kui on küsitavusi, kuidas ikka see otsimise muster toimib siis võid IntelliJ idea's selle ise järgi proovida.
*/

public class ClassFinder {

    List<CamelCaseMatcher> classNames = new ArrayList<CamelCaseMatcher>();

    public ClassFinder(InputStream classNamesStream)  {

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferReader = null;
        try {
            //TODO: Keep listening stream in separate thread?
            inputStreamReader = new InputStreamReader(classNamesStream, Charset.defaultCharset());
            bufferReader = new BufferedReader(inputStreamReader);

            String line = bufferReader.readLine();
            while (line != null) {
                classNames.add(new CamelCaseMatcher(getClassName(line), getPackageName(line)));
                line = bufferReader.readLine();
            }

            inputStreamReader.close();
        } catch (IOException e) {
            //TODO: Log it
            throw new ClassFinderException("Failed to read inputStream!!");
        } finally {
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(bufferReader);
        }
    }


    public Collection<String> findMatching(String pattern) {
        List<String> matchedFullClassNames = new ArrayList<String>();
        for (CamelCaseMatcher className : classNames) {
            if (className.match(pattern))
                matchedFullClassNames.add(className.getFullName());
        }
        sortClassNamesWithOutPackageName(matchedFullClassNames);
        return matchedFullClassNames;
    }

    void sortClassNamesWithOutPackageName(List<String> classNames) {
        Collections.sort(classNames, new Comparator<String>() {
            @Override
            public int compare(String fullClassName1, String fullClassName2) {
                return getClassName(fullClassName1).compareTo(getClassName(fullClassName2));
            }
        });
    }


    String getClassName(String classNameWithPackageName) {
        return classNameWithPackageName.substring(classNameWithPackageName.lastIndexOf('.') + 1);
    }

    String getPackageName(String classNameWithPackageName) {
        return classNameWithPackageName.substring(0, classNameWithPackageName.lastIndexOf('.') + 1);
    }


}
