(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.utils.log :refer [log log-o]]))


(defn app-content []
  (fn []
    [:div.app__content [:div.circled-button [:span.icon-home]]
     [:div.circled-button "X"]

     "    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac nisl tristique turpis commodo mattis. Sed
    faucibus, erat vitae luctus posuere, quam nibh pretium nunc, eu euismod erat velit et velit. Proin lacinia leo non
    dictum scelerisque. Aliquam mollis enim mauris, a interdum arcu posuere vitae. Vivamus eu luctus libero, eu
    fermentum ipsum. Integer et ornare erat. Nulla maximus neque orci. In ornare purus nibh, at hendrerit justo bibendum
    quis. Phasellus eu ultrices justo, quis mattis nunc. Mauris vitae ullamcorper leo. Etiam rhoncus ornare metus. Donec
    gravida velit in lectus sagittis fringilla. Pellentesque a interdum tortor. Fusce lacus elit, pharetra at feugiat
    at, malesuada id mi.
    Nulla quis aliquet libero. Quisque magna diam, malesuada sed vehicula eget, eleifend a mi. Quisque elementum euismod
    pellentesque. Sed id finibus nulla. Nulla dignissim hendrerit magna, nec porta orci. Fusce nec mi at est venenatis
    sollicitudin sed eget magna. Nam at urna sem. Etiam non sollicitudin ante. Duis laoreet erat tortor, non posuere
    risus dignissim in. Suspendisse sit amet mi eros. Aliquam sagittis porta leo, et semper nibh ullamcorper non. Mauris
    porttitor, augue vitae pellentesque lacinia, dui risus lacinia tortor, ac finibus velit ipsum sagittis purus.
    Nulla accumsan aliquam felis eget rutrum. Nam maximus arcu leo, vitae vulputate dui porta ut. Ut non sem nibh.
    Vivamus eleifend nisi nec ante imperdiet, et ultrices purus luctus. Proin augue nulla, semper sed felis ut,
    imperdiet venenatis urna. Vivamus finibus arcu vitae tortor maximus scelerisque. Nulla accumsan ipsum leo, sagittis
    pretium nibh mollis iaculis. Duis ac lacus vel magna consectetur accumsan lacinia vitae justo. In hac habitasse
    platea dictumst. Mauris nisl libero, ullamcorper semper est ut, faucibus mollis odio. Vestibulum ante ipsum primis
    in faucibus orci luctus et ultrices posuere cubilia Curae; Donec lacinia, massa et commodo imperdiet, lacus massa
    vulputate nibh, quis tincidunt sapien augue quis leo. Aliquam at vulputate arcu. Nullam sed tempus est, eu rutrum
    magna.
    Ut feugiat, sapien sed viverra fringilla, massa felis aliquet massa, a accumsan massa eros vel tortor. Nam mollis ac
    turpis quis posuere. Curabitur commodo sed arcu ut ultrices. Class aptent taciti sociosqu ad litora torquent per
    conubia nostra, per inceptos himenaeos. Nullam tristique mattis purus eu blandit. Fusce sit amet sollicitudin
    tortor. Fusce dignissim id ligula vitae accumsan. Suspendisse in leo pellentesque, tristique purus ut, porta dolor.
    Donec dapibus nisi at diam gravida, nec sollicitudin nibh vehicula. Aenean sollicitudin suscipit dui, quis volutpat
    ipsum tincidunt quis. In feugiat ex sit amet iaculis consequat.
    Cras neque diam, vulputate vel ultricies a, commodo nec elit. Quisque iaculis justo mi. Nullam eu diam eget felis
    dapibus sagittis id ac lectus. Proin pharetra quam bibendum arcu sagittis aliquam. Proin sed nisl malesuada,
    sagittis enim a, bibendum est. Integer eget sem in ipsum tincidunt accumsan id in mauris. Nullam quis magna sit amet
    velit laoreet viverra. Donec suscipit eros ipsum, at interdum lacus porttitor tempus. Donec quis mi posuere, auctor
    quam sed, consectetur est. Integer blandit elit sit amet venenatis accumsan. Etiam convallis leo metus, quis
    hendrerit risus tincidunt ut. Donec sed tortor odio. Phasellus eget lacus tellus. Maecenas ullamcorper mi sit amet
    massa sodales, eu egestas arcu cursus.
    Vestibulum non neque ac libero congue lobortis sit amet nec arcu. Aenean vel ipsum convallis, commodo lacus nec,
    posuere odio. Praesent lorem mauris, venenatis nec fringilla nec, egestas ut risus. Suspendisse in nunc augue. Cras
    eleifend turpis diam, vel euismod ex hendrerit ac. Duis nibh purus, imperdiet eu lacus id, auctor sollicitudin
    ipsum. Sed sit amet lacus tincidunt, efficitur leo a, facilisis lectus. Interdum et malesuada fames ac ante ipsum
    primis in faucibus. Nullam luctus elementum pellentesque. In non est quis nisi pharetra sollicitudin.
    Donec scelerisque elementum lorem varius finibus. Praesent ullamcorper, sem non blandit venenatis, erat nisl commodo
    lacus, a posuere dolor odio vel libero. Duis mollis neque et condimentum pulvinar. Vestibulum vestibulum sodales mi,
    non tincidunt ligula blandit vitae. Curabitur sodales, sapien nec tristique blandit, libero tellus molestie neque,
    in dictum ex urna ac turpis. Quisque convallis elementum libero, vel condimentum magna venenatis viverra. Fusce
    bibendum viverra luctus. Nullam dictum vehicula nulla, vitae aliquam mi varius vitae. Aliquam pulvinar sit amet quam
    vel tempor.
    Vestibulum in eleifend ante. Suspendisse elementum est nunc, at aliquet urna laoreet sed. Vivamus cursus, magna et
    venenatis efficitur, quam sem sollicitudin lacus, eget accumsan leo mi sit amet eros. Duis placerat nibh nunc, vel
    tempus odio dignissim non. Pellentesque at pellentesque lacus. Maecenas sed venenatis ante, dapibus aliquet turpis.
    Nunc rutrum euismod lorem non ultricies. Nulla ut eros et lacus consectetur mollis. Vivamus dictum luctus commodo.
    Nulla egestas eros sed urna fringilla, id ultricies libero finibus. Donec accumsan diam eu tortor posuere malesuada.
    Nulla facilisi. Sed luctus, ligula ut finibus consequat, ante velit fermentum metus, ac convallis metus lorem
    pretium orci. Vestibulum gravida purus nec dui congue lobortis. Mauris vulputate pulvinar nisi id aliquet. Quisque
    egestas fringilla purus. Cras at accumsan diam. Quisque lobortis fermentum metus, eget interdum massa auctor ac.
    Quisque aliquet nunc ut lorem aliquet elementum. Proin sit amet elementum magna. Phasellus eget felis dignissim,
    tempor mi ac, hendrerit tortor. Aenean nec augue eu enim auctor feugiat eget nec ligula. Sed iaculis quis dui ac
    placerat. Aliquam erat volutpat.
    Sed iaculis lobortis sagittis. Maecenas posuere sagittis mauris a luctus. Ut dictum vulputate venenatis. Mauris sit
    amet laoreet erat. Integer porta diam lorem, nec hendrerit nisl posuere in. Phasellus dapibus commodo dui nec
    mollis. Praesent scelerisque nulla pulvinar lacus tincidunt euismod. Nulla sapien leo, dapibus a interdum a,
    sollicitudin a ex. Pellentesque ac enim sit amet est rhoncus elementum. Quisque sollicitudin non orci ac
    scelerisque. Aenean iaculis sit amet nunc in vehicula. Donec a tellus pellentesque, tincidunt lorem tincidunt,
    imperdiet ante. Maecenas velit nulla, tristique eget egestas ut, euismod a ipsum. Morbi vel mi nec diam lacinia
    imperdiet quis ac felis. Vivamus velit mi, feugiat sed vulputate a, ultricies ac sapien. Pellentesque habitant morbi
    tristique senectus et netus et malesuada fames ac turpis egestas.
    Nam tristique eget dui eu mattis. Aenean facilisis, arcu ut aliquet suscipit, felis ante dapibus lectus, sit amet
    rutrum enim sapien a ipsum. Ut vulputate velit at felis malesuada ornare ut quis eros. In sed justo diam. Vestibulum
    congue, enim ac interdum pellentesque, diam ligula egestas nibh, ut hendrerit magna massa in ex. Proin porta ex a
    metus faucibus, eget ultricies odio congue. Curabitur sed elit lacus. Vivamus sed sem eget ligula porttitor luctus.
    Mauris volutpat, nibh ut imperdiet vehicula, neque ex sagittis diam, sed egestas massa felis sed odio. Donec iaculis
    rutrum libero, ac eleifend ante mollis quis. In hac habitasse platea dictumst. Nam porta dui at est scelerisque, a
    dapibus ante rhoncus. Aenean blandit dui in lectus ullamcorper, molestie consectetur est hendrerit. Donec a nisi sit
    amet metus tempus commodo at a lorem. Proin congue lorem vitae lacus semper fringilla.
    Nam auctor, lacus non venenatis hendrerit, urna enim placerat magna, id mollis lacus diam ultrices ex. Aenean
    tristique sed risus in scelerisque. Donec sodales, nisi consequat sagittis tristique, erat diam ultricies diam, vel
    tincidunt lacus est et nunc. Phasellus ut scelerisque velit. Etiam laoreet laoreet nulla, sed scelerisque dolor
    convallis id. Curabitur ac placerat risus, viverra auctor nunc. Fusce sollicitudin ac arcu sed varius. Maecenas
    luctus venenatis ex, vel mollis elit hendrerit nec. Vestibulum eget vestibulum eros, lacinia suscipit elit. Sed in
    vulputate magna, eget ullamcorper erat.
    Sed ut massa lorem. Nullam viverra massa a quam molestie, ut pharetra orci vestibulum. Etiam id massa pulvinar,
    commodo risus at, vehicula felis. Aenean luctus sapien dolor, a lacinia leo vulputate vel. Nunc ex ante, vehicula a
    laoreet non, faucibus sed mi. Nunc eget nunc viverra sem efficitur pretium et eu nisi. Pellentesque semper enim a
    ornare suscipit. Suspendisse venenatis purus in mauris molestie, ut efficitur tortor porta.
    Etiam sem diam, ultrices at commodo eget, iaculis mollis nunc. Ut eget leo ipsum. Praesent placerat sapien ut
    commodo eleifend. Vestibulum molestie laoreet accumsan. Nunc posuere pulvinar lectus, id congue nulla gravida vel.
    Ut sagittis condimentum mauris, vel semper ligula iaculis quis. Donec quis porttitor arcu.
    Praesent at ultrices augue. Sed tellus orci, posuere elementum est sit amet, iaculis imperdiet justo. Duis sit amet
    accumsan nisi, ac sollicitudin nisl. Nullam efficitur, ex ut tincidunt porta, erat nibh imperdiet erat, et imperdiet
    nibh sem vel sapien. Vestibulum eleifend metus vitae lacus pulvinar aliquet. Ut nec sem vehicula, euismod dui sit
    amet, bibendum tellus. Phasellus nisl est, efficitur id augue id, fermentum rutrum metus. In erat enim, fringilla id
    faucibus in, pretium a mi.
    Phasellus augue lacus, lobortis id lectus in, molestie condimentum felis. Integer commodo volutpat tellus, eu
    condimentum arcu porttitor quis. Duis posuere augue eget tellus malesuada mattis. Sed consequat eu urna consectetur
    mattis. Vivamus fringilla nulla at odio mollis varius. Morbi sed bibendum nisi. Ut consequat eu massa eu lobortis.
    Aliquam laoreet efficitur massa, et auctor lectus. Etiam feugiat mi eu magna molestie faucibus at ac nibh. Aliquam
    suscipit porta dignissim.
    Nullam fermentum ex vel eros aliquam, at consequat sem elementum. Quisque sit amet enim ante. Aliquam interdum
    lobortis tellus, quis tempor sem iaculis at. Morbi aliquet, ex sit amet varius pulvinar, erat purus eleifend metus,
    eu ultricies urna sapien elementum ex. Etiam fermentum elit sit amet volutpat facilisis. Aenean ut augue a risus
    iaculis commodo. Quisque id tellus ac odio luctus vehicula in eget mi. Nulla mattis augue ut velit tincidunt, vitae
    varius turpis feugiat. Aenean facilisis pulvinar erat eget ultrices. Proin vel dapibus arcu. Proin sodales ligula
    quis ex tristique, sit amet sollicitudin massa venenatis. Mauris maximus nulla nec commodo mattis. Nullam nunc sem,
    auctor id arcu quis, ornare lacinia lorem. Nulla sed metus semper, blandit quam eu, ultricies massa.
    Ut ac ligula in velit blandit iaculis. Maecenas purus mi, hendrerit ut lectus sed, consectetur cursus mi. Nullam sit
    amet porttitor dui, non dictum diam. Pellentesque nec ullamcorper sapien, nec rutrum lacus. Vestibulum molestie nunc
    mi, et convallis odio finibus dignissim. Nulla non erat sodales, ornare augue non, laoreet metus. Nulla eleifend
    dictum hendrerit. Sed sit amet diam sapien. Nulla facilisi.
    Proin non quam nisi. Vestibulum aliquam mauris ligula, non efficitur libero blandit vel. Maecenas tincidunt
    fermentum leo, eget luctus magna viverra sit amet. Duis sem ex, eleifend in velit nec, volutpat pulvinar magna.
    Donec porta porta tellus sit amet vulputate. Aenean auctor tortor ipsum, vitae sagittis tellus ultrices quis.
    Praesent ultricies pharetra nulla et mollis. Pellentesque posuere libero nec interdum laoreet. Donec ac odio vitae
    ex condimentum efficitur. Sed vel ultrices turpis.
    Vivamus quis auctor metus, ut mollis nisl. Etiam convallis hendrerit diam, a finibus velit molestie id. Etiam id
    lacinia neque. Suspendisse id varius urna, id iaculis quam. Etiam lacinia magna bibendum metus convallis, maximus
    feugiat odio condimentum. Ut non eros odio. Nunc euismod iaculis erat. Pellentesque accumsan, ligula a suscipit
    placerat, turpis sem finibus mauris, at feugiat mauris nisl et arcu.    faucibus, erat vitae luctus posuere, quam nibh pretium nunc, eu euismod erat velit et velit. Proin lacinia leo non
    dictum scelerisque. Aliquam mollis enim mauris, a interdum arcu posuere vitae. Vivamus eu luctus libero, eu
    fermentum ipsum. Integer et ornare erat. Nulla maximus neque orci. In ornare purus nibh, at hendrerit justo bibendum
    quis. Phasellus eu ultrices justo, quis mattis nunc. Mauris vitae ullamcorper leo. Etiam rhoncus ornare metus. Donec
    gravida velit in lectus sagittis fringilla. Pellentesque a interdum tortor. Fusce lacus elit, pharetra at feugiat
    at, malesuada id mi.
    Nulla quis aliquet libero. Quisque magna diam, malesuada sed vehicula eget, eleifend a mi. Quisque elementum euismod
    pellentesque. Sed id finibus nulla. Nulla dignissim hendrerit magna, nec porta orci. Fusce nec mi at est venenatis
    sollicitudin sed eget magna. Nam at urna sem. Etiam non sollicitudin ante. Duis laoreet erat tortor, non posuere
    risus dignissim in. Suspendisse sit amet mi eros. Aliquam sagittis porta leo, et semper nibh ullamcorper non. Mauris
    porttitor, augue vitae pellentesque lacinia, dui risus lacinia tortor, ac finibus velit ipsum sagittis purus.
    Nulla accumsan aliquam felis eget rutrum. Nam maximus arcu leo, vitae vulputate dui porta ut. Ut non sem nibh.
    Vivamus eleifend nisi nec ante imperdiet, et ultrices purus luctus. Proin augue nulla, semper sed felis ut,
    imperdiet venenatis urna. Vivamus finibus arcu vitae tortor maximus scelerisque. Nulla accumsan ipsum leo, sagittis
    pretium nibh mollis iaculis. Duis ac lacus vel magna consectetur accumsan lacinia vitae justo. In hac habitasse
    platea dictumst. Mauris nisl libero, ullamcorper semper est ut, faucibus mollis odio. Vestibulum ante ipsum primis
    in faucibus orci luctus et ultrices posuere cubilia Curae; Donec lacinia, massa et commodo imperdiet, lacus massa
    vulputate nibh, quis tincidunt sapien augue quis leo. Aliquam at vulputate arcu. Nullam sed tempus est, eu rutrum
    magna.
    Ut feugiat, sapien sed viverra fringilla, massa felis aliquet massa, a accumsan massa eros vel tortor. Nam mollis ac
    turpis quis posuere. Curabitur commodo sed arcu ut ultrices. Class aptent taciti sociosqu ad litora torquent per
    conubia nostra, per inceptos himenaeos. Nullam tristique mattis purus eu blandit. Fusce sit amet sollicitudin
    tortor. Fusce dignissim id ligula vitae accumsan. Suspendisse in leo pellentesque, tristique purus ut, porta dolor.
    Donec dapibus nisi at diam gravida, nec sollicitudin nibh vehicula. Aenean sollicitudin suscipit dui, quis volutpat
    ipsum tincidunt quis. In feugiat ex sit amet iaculis consequat.
    Cras neque diam, vulputate vel ultricies a, commodo nec elit. Quisque iaculis justo mi. Nullam eu diam eget felis
    dapibus sagittis id ac lectus. Proin pharetra quam bibendum arcu sagittis aliquam. Proin sed nisl malesuada,
    sagittis enim a, bibendum est. Integer eget sem in ipsum tincidunt accumsan id in mauris. Nullam quis magna sit amet
    velit laoreet viverra. Donec suscipit eros ipsum, at interdum lacus porttitor tempus. Donec quis mi posuere, auctor
    quam sed, consectetur est. Integer blandit elit sit amet venenatis accumsan. Etiam convallis leo metus, quis
    hendrerit risus tincidunt ut. Donec sed tortor odio. Phasellus eget lacus tellus. Maecenas ullamcorper mi sit amet
    massa sodales, eu egestas arcu cursus.
    Vestibulum non neque ac libero congue lobortis sit amet nec arcu. Aenean vel ipsum convallis, commodo lacus nec,
    posuere odio. Praesent lorem mauris, venenatis nec fringilla nec, egestas ut risus. Suspendisse in nunc augue. Cras
    eleifend turpis diam, vel euismod ex hendrerit ac. Duis nibh purus, imperdiet eu lacus id, auctor sollicitudin
    ipsum. Sed sit amet lacus tincidunt, efficitur leo a, facilisis lectus. Interdum et malesuada fames ac ante ipsum
    primis in faucibus. Nullam luctus elementum pellentesque. In non est quis nisi pharetra sollicitudin.
    Donec scelerisque elementum lorem varius finibus. Praesent ullamcorper, sem non blandit venenatis, erat nisl commodo
    lacus, a posuere dolor odio vel libero. Duis mollis neque et condimentum pulvinar. Vestibulum vestibulum sodales mi,
    non tincidunt ligula blandit vitae. Curabitur sodales, sapien nec tristique blandit, libero tellus molestie neque,
    in dictum ex urna ac turpis. Quisque convallis elementum libero, vel condimentum magna venenatis viverra. Fusce
    bibendum viverra luctus. Nullam dictum vehicula nulla, vitae aliquam mi varius vitae. Aliquam pulvinar sit amet quam
    vel tempor.
    Vestibulum in eleifend ante. Suspendisse elementum est nunc, at aliquet urna laoreet sed. Vivamus cursus, magna et
    venenatis efficitur, quam sem sollicitudin lacus, eget accumsan leo mi sit amet eros. Duis placerat nibh nunc, vel
    tempus odio dignissim non. Pellentesque at pellentesque lacus. Maecenas sed venenatis ante, dapibus aliquet turpis.
    Nunc rutrum euismod lorem non ultricies. Nulla ut eros et lacus consectetur mollis. Vivamus dictum luctus commodo.
    Nulla egestas eros sed urna fringilla, id ultricies libero finibus. Donec accumsan diam eu tortor posuere malesuada.
    Nulla facilisi. Sed luctus, ligula ut finibus consequat, ante velit fermentum metus, ac convallis metus lorem
    pretium orci. Vestibulum gravida purus nec dui congue lobortis. Mauris vulputate pulvinar nisi id aliquet. Quisque
    egestas fringilla purus. Cras at accumsan diam. Quisque lobortis fermentum metus, eget interdum massa auctor ac.
    Quisque aliquet nunc ut lorem aliquet elementum. Proin sit amet elementum magna. Phasellus eget felis dignissim,
    tempor mi ac, hendrerit tortor. Aenean nec augue eu enim auctor feugiat eget nec ligula. Sed iaculis quis dui ac
    placerat. Aliquam erat volutpat.
    Sed iaculis lobortis sagittis. Maecenas posuere sagittis mauris a luctus. Ut dictum vulputate venenatis. Mauris sit
    amet laoreet erat. Integer porta diam lorem, nec hendrerit nisl posuere in. Phasellus dapibus commodo dui nec
    mollis. Praesent scelerisque nulla pulvinar lacus tincidunt euismod. Nulla sapien leo, dapibus a interdum a,
    sollicitudin a ex. Pellentesque ac enim sit amet est rhoncus elementum. Quisque sollicitudin non orci ac
    scelerisque. Aenean iaculis sit amet nunc in vehicula. Donec a tellus pellentesque, tincidunt lorem tincidunt,
    imperdiet ante. Maecenas velit nulla, tristique eget egestas ut, euismod a ipsum. Morbi vel mi nec diam lacinia
    imperdiet quis ac felis. Vivamus velit mi, feugiat sed vulputate a, ultricies ac sapien. Pellentesque habitant morbi
    tristique senectus et netus et malesuada fames ac turpis egestas.
    Nam tristique eget dui eu mattis. Aenean facilisis, arcu ut aliquet suscipit, felis ante dapibus lectus, sit amet
    rutrum enim sapien a ipsum. Ut vulputate velit at felis malesuada ornare ut quis eros. In sed justo diam. Vestibulum
    congue, enim ac interdum pellentesque, diam ligula egestas nibh, ut hendrerit magna massa in ex. Proin porta ex a
    metus faucibus, eget ultricies odio congue. Curabitur sed elit lacus. Vivamus sed sem eget ligula porttitor luctus.
    Mauris volutpat, nibh ut imperdiet vehicula, neque ex sagittis diam, sed egestas massa felis sed odio. Donec iaculis
    rutrum libero, ac eleifend ante mollis quis. In hac habitasse platea dictumst. Nam porta dui at est scelerisque, a
    dapibus ante rhoncus. Aenean blandit dui in lectus ullamcorper, molestie consectetur est hendrerit. Donec a nisi sit
    amet metus tempus commodo at a lorem. Proin congue lorem vitae lacus semper fringilla.
    Nam auctor, lacus non venenatis hendrerit, urna enim placerat magna, id mollis lacus diam ultrices ex. Aenean
    tristique sed risus in scelerisque. Donec sodales, nisi consequat sagittis tristique, erat diam ultricies diam, vel
    tincidunt lacus est et nunc. Phasellus ut scelerisque velit. Etiam laoreet laoreet nulla, sed scelerisque dolor
    convallis id. Curabitur ac placerat risus, viverra auctor nunc. Fusce sollicitudin ac arcu sed varius. Maecenas
    luctus venenatis ex, vel mollis elit hendrerit nec. Vestibulum eget vestibulum eros, lacinia suscipit elit. Sed in
    vulputate magna, eget ullamcorper erat.
    Sed ut massa lorem. Nullam viverra massa a quam molestie, ut pharetra orci vestibulum. Etiam id massa pulvinar,
    commodo risus at, vehicula felis. Aenean luctus sapien dolor, a lacinia leo vulputate vel. Nunc ex ante, vehicula a
    laoreet non, faucibus sed mi. Nunc eget nunc viverra sem efficitur pretium et eu nisi. Pellentesque semper enim a
    ornare suscipit. Suspendisse venenatis purus in mauris molestie, ut efficitur tortor porta.
    Etiam sem diam, ultrices at commodo eget, iaculis mollis nunc. Ut eget leo ipsum. Praesent placerat sapien ut
    commodo eleifend. Vestibulum molestie laoreet accumsan. Nunc posuere pulvinar lectus, id congue nulla gravida vel.
    Ut sagittis condimentum mauris, vel semper ligula iaculis quis. Donec quis porttitor arcu.
    Praesent at ultrices augue. Sed tellus orci, posuere elementum est sit amet, iaculis imperdiet justo. Duis sit amet
    accumsan nisi, ac sollicitudin nisl. Nullam efficitur, ex ut tincidunt porta, erat nibh imperdiet erat, et imperdiet
    nibh sem vel sapien. Vestibulum eleifend metus vitae lacus pulvinar aliquet. Ut nec sem vehicula, euismod dui sit
    amet, bibendum tellus. Phasellus nisl est, efficitur id augue id, fermentum rutrum metus. In erat enim, fringilla id
    faucibus in, pretium a mi.
    Phasellus augue lacus, lobortis id lectus in, molestie condimentum felis. Integer commodo volutpat tellus, eu
    condimentum arcu porttitor quis. Duis posuere augue eget tellus malesuada mattis. Sed consequat eu urna consectetur
    mattis. Vivamus fringilla nulla at odio mollis varius. Morbi sed bibendum nisi. Ut consequat eu massa eu lobortis.
    Aliquam laoreet efficitur massa, et auctor lectus. Etiam feugiat mi eu magna molestie faucibus at ac nibh. Aliquam
    suscipit porta dignissim.
    Nullam fermentum ex vel eros aliquam, at consequat sem elementum. Quisque sit amet enim ante. Aliquam interdum
    lobortis tellus, quis tempor sem iaculis at. Morbi aliquet, ex sit amet varius pulvinar, erat purus eleifend metus,
    eu ultricies urna sapien elementum ex. Etiam fermentum elit sit amet volutpat facilisis. Aenean ut augue a risus
    iaculis commodo. Quisque id tellus ac odio luctus vehicula in eget mi. Nulla mattis augue ut velit tincidunt, vitae
    varius turpis feugiat. Aenean facilisis pulvinar erat eget ultrices. Proin vel dapibus arcu. Proin sodales ligula
    quis ex tristique, sit amet sollicitudin massa venenatis. Mauris maximus nulla nec commodo mattis. Nullam nunc sem,
    auctor id arcu quis, ornare lacinia lorem. Nulla sed metus semper, blandit quam eu, ultricies massa.
    Ut ac ligula in velit blandit iaculis. Maecenas purus mi, hendrerit ut lectus sed, consectetur cursus mi. Nullam sit
    amet porttitor dui, non dictum diam. Pellentesque nec ullamcorper sapien, nec rutrum lacus. Vestibulum molestie nunc
    mi, et convallis odio finibus dignissim. Nulla non erat sodales, ornare augue non, laoreet metus. Nulla eleifend
    dictum hendrerit. Sed sit amet diam sapien. Nulla facilisi.
    Proin non quam nisi. Vestibulum aliquam mauris ligula, non efficitur libero blandit vel. Maecenas tincidunt
    fermentum leo, eget luctus magna viverra sit amet. Duis sem ex, eleifend in velit nec, volutpat pulvinar magna.
    Donec porta porta tellus sit amet vulputate. Aenean auctor tortor ipsum, vitae sagittis tellus ultrices quis.
    Praesent ultricies pharetra nulla et mollis. Pellentesque posuere libero nec interdum laoreet. Donec ac odio vitae
    ex condimentum efficitur. Sed vel ultrices turpis.
    Vivamus quis auctor metus, ut mollis nisl. Etiam convallis hendrerit diam, a finibus velit molestie id. Etiam id
    lacinia neque. Suspendisse id varius urna, id iaculis quam. Etiam lacinia magna bibendum metus convallis, maximus
    feugiat odio condimentum. Ut non eros odio. Nunc euismod iaculis erat. Pellentesque accumsan, ligula a suscipit
    placerat, turpis sem finibus mauris, at feugiat mauris nisl et arcu.
"]))

(def app-content-nicescroll
  (with-meta app-content
             {:component-did-mount #(.niceScroll (js/$ (r/dom-node %)))}))