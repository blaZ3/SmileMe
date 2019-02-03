package me.tellvivk.smileme.helpers

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.tellvivk.smileme.app.model.Image

class TestHelper {

    companion object {

        val DUMMY_JSON =
            "[ { \"comment\": \"Est sint dolor veniam aliquip velit non anim et aliquip in ad ullamco. Labore occaecat ullamco in sunt culpa eiusmod reprehenderit reprehenderit sit. Elit irure in commodo consectetur laboris. Pariatur voluptate ipsum laboris cupidatat voluptate amet in culpa.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b54080af7f1ea07123\", \"publishedAt\": \"2016-09-19T04:14:06 -02:00\", \"title\": \"Adriana Rowe\" }, { \"comment\": \"Dolore reprehenderit ullamco ut duis sunt qui eu laborum esse sint. Voluptate et incididunt incididunt cillum ex. Sit cillum pariatur deserunt excepteur voluptate nostrud occaecat incididunt irure amet officia duis incididunt id. Nulla duis commodo amet officia.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5778782c5fcf83179\", \"publishedAt\": \"2017-05-10T12:17:28 -02:00\", \"title\": \"Melissa Meadows\" }, { \"comment\": \"Ad nostrud Lorem et ex amet tempor. In fugiat dolore aliquip deserunt occaecat ut officia eu in ullamco cupidatat consectetur. Cupidatat ea voluptate aliqua aute excepteur quis exercitation magna nisi ex proident labore culpa adipisicing. Veniam sunt cupidatat proident non esse mollit.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5baeb0fc43b46682c\", \"publishedAt\": \"2014-09-06T04:42:16 -02:00\", \"title\": \"Myers Bates\" }, { \"comment\": \"Dolor Lorem ea pariatur do. Nisi laborum non nostrud eiusmod minim laborum nisi elit. Voluptate qui et qui in nulla sit duis veniam reprehenderit nostrud velit exercitation consectetur qui. Velit ipsum cupidatat ad laborum labore commodo dolor exercitation duis excepteur minim aliquip. Nostrud est eiusmod quis dolore deserunt excepteur id elit.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b57d40290c5c7466cf\", \"publishedAt\": \"2014-11-22T01:18:25 -01:00\", \"title\": \"Carly Whitney\" }, { \"comment\": \"Amet pariatur elit non aliqua nostrud tempor esse. Exercitation velit officia cupidatat irure esse id cupidatat enim enim enim. Exercitation amet ad ullamco nulla incididunt. Anim cupidatat irure officia amet incididunt aliqua aliquip fugiat magna cillum nisi.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5d15f6ca7546dbb8d\", \"publishedAt\": \"2017-05-28T11:46:18 -02:00\", \"title\": \"Aline Daniels\" }, { \"comment\": \"Elit elit tempor id nulla commodo quis ut anim id est reprehenderit veniam. Elit deserunt cillum sint in ex magna ex aliquip adipisicing nostrud. Elit deserunt cupidatat ad consequat irure occaecat sunt voluptate eiusmod. Sunt aute sint amet sit culpa. Aliquip nostrud labore reprehenderit pariatur consectetur magna excepteur dolore deserunt adipisicing. Laborum consectetur adipisicing amet pariatur. Culpa in nulla cillum proident sit irure et dolor qui laborum Lorem consequat.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b51c5819abd540b6d2\", \"publishedAt\": \"2017-04-01T02:33:30 -02:00\", \"title\": \"Annabelle Bolton\" }, { \"comment\": \"In ex eu ea ullamco esse et incididunt sint et. Aliqua sit cillum esse deserunt dolore cupidatat. Aliquip quis labore adipisicing adipisicing ullamco duis dolore est ipsum ea eiusmod officia incididunt culpa.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b59db83bc10aa754f9\", \"publishedAt\": \"2014-06-12T08:04:07 -02:00\", \"title\": \"Lucas Armstrong\" }, { \"comment\": \"Ut voluptate consequat magna laborum aliquip consequat deserunt culpa veniam officia reprehenderit laboris. Labore exercitation mollit aliqua deserunt sint. Aliquip duis eu consequat aliqua commodo eu. Ullamco exercitation commodo elit dolor qui proident anim aliqua cillum deserunt est excepteur proident. In elit veniam cupidatat laboris qui non consectetur labore est ea. Proident minim fugiat labore irure.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b54f6e8c5b9850b745\", \"publishedAt\": \"2015-10-09T05:50:59 -02:00\", \"title\": \"Adams Stark\" }, { \"comment\": \"Occaecat deserunt incididunt mollit exercitation Lorem. Fugiat qui aliqua deserunt dolore sit nostrud anim eiusmod non anim voluptate. Elit adipisicing dolor ex non qui eiusmod id culpa voluptate aliqua enim. Minim consectetur est exercitation nisi nostrud in do consectetur. Incididunt amet labore elit et excepteur ipsum reprehenderit ipsum. Irure esse eu est ut officia amet. Officia magna cupidatat cillum anim commodo ex anim.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b534cfd49454dfa1f0\", \"publishedAt\": \"2017-06-27T04:28:12 -02:00\", \"title\": \"Joann Short\" }, { \"comment\": \"Velit eiusmod magna fugiat proident sunt velit consectetur Lorem aliqua incididunt pariatur aliqua et labore. Anim Lorem exercitation exercitation labore ad amet excepteur fugiat pariatur ea in consectetur. Adipisicing nisi esse dolore est consequat cillum ut commodo eu est non quis ex eiusmod. Sint nulla consectetur occaecat aliquip aliquip anim excepteur Lorem sunt.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5ce71e460132570f8\", \"publishedAt\": \"2015-05-02T11:35:16 -02:00\", \"title\": \"Gale Morse\" }, { \"comment\": \"Labore esse cillum ex laboris tempor ipsum in tempor nisi esse quis duis esse deserunt. Elit duis qui cupidatat pariatur. Dolore proident reprehenderit id proident veniam est anim aute tempor esse do. Sunt voluptate laboris voluptate consectetur non deserunt amet.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b505748f58f18de63b\", \"publishedAt\": \"2014-01-21T05:21:10 -01:00\", \"title\": \"Ramos Robbins\" }, { \"comment\": \"Cupidatat nulla est est aliquip consequat reprehenderit ullamco laborum. Velit labore labore ut eu labore. Eu ex quis cillum voluptate ea ullamco et aliqua nostrud esse et. Eu occaecat mollit Lorem mollit dolore esse dolor excepteur laborum anim. Mollit cillum dolor laboris laborum amet. Enim culpa consequat sint laboris velit pariatur adipisicing eu in ea.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b56b4486aaef51adf6\", \"publishedAt\": \"2018-10-19T03:32:42 -02:00\", \"title\": \"Soto Sanders\" }, { \"comment\": \"Eu ex anim ut laborum deserunt commodo laboris. Cillum est nisi cupidatat tempor ad ullamco Lorem ut exercitation non deserunt sit ad enim. In excepteur voluptate aliqua pariatur id incididunt excepteur aliqua. Nostrud culpa ex aliquip ipsum consequat cillum do ex aliqua commodo elit. Consectetur officia mollit veniam esse consequat enim do ea quis dolore eu duis tempor. Ut labore cupidatat tempor eiusmod aute esse dolore ea. Eu adipisicing magna sunt deserunt eiusmod amet velit velit.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5bbf23d33b9a0ea6d\", \"publishedAt\": \"2016-05-22T08:24:31 -02:00\", \"title\": \"Bettye Fulton\" }, { \"comment\": \"Quis aliquip mollit est anim cupidatat nostrud Lorem sint minim exercitation sunt nulla voluptate. Cillum incididunt aute veniam incididunt aute commodo do labore. Cillum anim anim nisi incididunt sint sint id aliqua ipsum sit.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5e57b9138b7baf6d6\", \"publishedAt\": \"2018-11-18T05:54:16 -01:00\", \"title\": \"Marks Frye\" }, { \"comment\": \"Elit incididunt ut tempor adipisicing magna occaecat. In duis ex officia laboris non adipisicing. Incididunt voluptate ullamco in nostrud mollit quis sint ipsum quis ex minim. Qui in velit veniam magna magna irure culpa dolore Lorem sunt.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b58ed7a1768a56cc2b\", \"publishedAt\": \"2016-11-23T05:46:42 -01:00\", \"title\": \"Bonita Mckinney\" }, { \"comment\": \"Id proident aute Lorem duis occaecat labore deserunt aliqua quis ut et quis. Occaecat nisi fugiat ullamco sunt Lorem mollit laboris. Nisi fugiat adipisicing nostrud anim do commodo consectetur minim tempor eu mollit officia aute. Velit cupidatat nisi cillum do ipsum Lorem in do do. Duis ad voluptate culpa mollit nostrud exercitation. Sit tempor nulla cillum minim anim laborum consequat in non quis commodo occaecat magna. Sint irure commodo consectetur qui reprehenderit officia occaecat exercitation dolor sunt reprehenderit.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b51bc725a3bc182b65\", \"publishedAt\": \"2017-03-15T07:07:48 -01:00\", \"title\": \"Rhea Cobb\" }, { \"comment\": \"Deserunt cillum irure pariatur aliquip dolor ut mollit ea laborum. Veniam deserunt veniam non exercitation laborum ullamco laboris aliqua proident esse. Minim exercitation velit aliqua fugiat ipsum veniam. Anim voluptate deserunt sunt sit sit non nisi. Nostrud in tempor ad labore nisi adipisicing ullamco eiusmod tempor eu labore in. Sint qui eu in excepteur irure Lorem do duis ut et deserunt magna aliqua esse. Quis ad aliquip sunt ex adipisicing non aliqua est.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b59983248b7e667d90\", \"publishedAt\": \"2017-08-26T10:20:43 -02:00\", \"title\": \"Simmons Ratliff\" }, { \"comment\": \"Amet cillum eiusmod dolore sunt reprehenderit reprehenderit aliqua sit ad id velit mollit officia. Excepteur adipisicing reprehenderit quis aliquip duis ut ad amet tempor occaecat. Aute nulla nisi mollit ut eu ullamco occaecat tempor anim eu cupidatat laboris.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5ac2ab027ce8158b0\", \"publishedAt\": \"2018-04-19T11:53:15 -02:00\", \"title\": \"Mona Bradley\" }, { \"comment\": \"Duis exercitation laborum elit culpa ad occaecat. Aliquip deserunt fugiat in laboris fugiat incididunt ea dolor adipisicing mollit esse reprehenderit velit fugiat. Do sit ad voluptate sunt adipisicing occaecat occaecat sunt enim et sunt ad. Cupidatat in incididunt dolor incididunt ipsum enim nulla excepteur id exercitation culpa. Ex in elit exercitation exercitation aliquip sint tempor laborum. Laborum culpa dolore consectetur incididunt est laboris dolore.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b5c98b7bea22897b70\", \"publishedAt\": \"2016-11-17T05:33:33 -01:00\", \"title\": \"Robert Carrillo\" }, { \"comment\": \"Mollit velit irure anim enim esse mollit cupidatat aliquip magna ullamco incididunt occaecat dolore. Laborum aliquip ex mollit in esse nostrud dolore. Commodo sint ea proident anim id dolore consectetur minim consectetur nulla id excepteur. Velit enim id magna minim in anim est aliqua.\\r\\n\", \"picture\": \"https://unsplash.it/600/300/?random\", \"_id\": \"5c31e8b50ea3231118b3ee4e\", \"publishedAt\": \"2018-07-04T06:09:44 -02:00\", \"title\": \"Marlene Sherman\" } ]"

        const val DUMMY_DELAY = 2500L

        const val TEST_STRING = "TEST_STRING"
        const val TEST_QUERY = "TEST_QUERY"
        const val TEST_PAGE = 1

        const val API_KEY = "635ad7a2"


        fun getDummyImages(): List<Image> {
            return GsonBuilder().serializeNulls().create()
                .fromJson(
                    DUMMY_JSON,
                    object : TypeToken<List<Image>>() {}.type
                )
        }

    }

}