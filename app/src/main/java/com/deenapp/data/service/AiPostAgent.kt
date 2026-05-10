package com.deenapp.data.service

import com.deenapp.data.model.Post
import com.deenapp.data.model.PostVisibility
import com.deenapp.data.repository.DeenRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class AiPostAgent @Inject constructor(
    private val repository: DeenRepository
) {
    private val urduPosts = listOf(
        "اللہ تعالیٰ نے فرمایا: 'اور جو مجھ سے ڈرے گا، اللہ اس کے لیے نکلنے کی راہ بنا دے گا۔' (سورۃ الطلاق: 2)",
        "نبی کریم ﷺ نے فرمایا: 'مسکراہٹ صدقہ ہے۔' - ہر کسی کے ساتھ مسکرا کر بات کریں۔",
        "اللہ تعالیٰ کا فرمان: 'بے شک نماز فحشاء اور منکر سے روکتی ہے۔' (سورۃ العنکبوت: 45)",
        "رسول اللہ ﷺ نے فرمایا: 'سب سے بہترین لوگ وہ ہیں جو دوسروں کے لیے سب سے زیادہ فائدہ مند ہوں۔'",
        "صبر کرنے والوں کو بے حساب اجر دیا جائے گا۔ (سورۃ الزمر: 10)",
        "نبی کریم ﷺ نے فرمایا: 'جو شخص اللہ پر اور آخرت کے دن پر ایمان رکھتا ہے، اسے چاہیے کہ اچھی بات کرے یا خاموش رہے۔'",
        "ذکرِ الٰہی سے دلوں کو سکون ملتا ہے۔ سبحان اللہ، الحمدللہ، اللہ اکبر 🤲",
        "اللہ کی رحمت سے مایوس نہ ہو، بے شک اللہ تمام گناہ معاف کر دیتا ہے۔ (سورۃ الزمر: 53)"
    )

    private val englishPosts = listOf(
        "The Prophet ﷺ said: 'The best of you are those who are best to their families.' - Let's practice kindness at home first.",
        "Allah says: 'Indeed, with hardship comes ease.' (Quran 94:6) - Never lose hope in Allah's mercy.",
        "The Prophet ﷺ said: 'Whoever believes in Allah and the Last Day, let him speak good or remain silent.' - Words carry weight.",
        "Start your morning with Bismillah and end your day with Alhamdulillah. Every moment is a blessing from Allah.",
        "'And He found you lost and guided you.' (Quran 93:7) - Allah's guidance is the greatest gift.",
        "The Prophet ﷺ said: 'Kindness is a mark of faith.' - Be kind to everyone you meet today.",
        "Surah Al-Fatiha reminds us: All praise belongs to Allah, the Lord of all worlds. Begin everything with gratitude.",
        "The Prophet ﷺ said: 'Make things easy and do not make them difficult.' - Simplicity is the essence of Islam."
    )

    private val arabicPosts = listOf(
        "قال الله تعالى: ﴿إِنَّ مَعَ الْعُسْرِ يُسْرًا﴾ - لا تفقد الأمل أبداً",
        "قال رسول الله ﷺ: 'تبسُّمك في وجه أخيك صدقة' - ابتسم دائماً",
        "قال الله تعالى: ﴿وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا﴾ - التقوى مفتاح الفرج",
        "قال رسول الله ﷺ: 'خيركم من تعلم القرآن وعلمه' - تعلم القرآن واعمل به",
        "﴿أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ﴾ - اذكر الله يطمئن قلبك",
        "قال رسول الله ﷺ: 'الدين النصيحة' - كن ناصحاً أميناً",
        "﴿وَقُل رَّبِّ زِدْنِي عِلْمًا﴾ - اطلب العلم من المهد إلى اللحد",
        "قال رسول الله ﷺ: 'من سلك طريقاً يلتمس فيه علماً سهّل الله له به طريقاً إلى الجنة'"
    )

    private val agentNames = listOf(
        "Deen Wisdom" to "deen_wisdom",
        "Islamic Reminders" to "islamic_reminders",
        "Quran Daily" to "quran_daily",
        "Hadith Corner" to "hadith_corner",
        "Deen Inspiration" to "deen_inspiration"
    )

    fun generatePosts(count: Int = 5) {
        repeat(count) { index ->
            val language = index % 3
            val content = when (language) {
                0 -> urduPosts[Random.nextInt(urduPosts.size)]
                1 -> englishPosts[Random.nextInt(englishPosts.size)]
                else -> arabicPosts[Random.nextInt(arabicPosts.size)]
            }

            val agent = agentNames[index % agentNames.size]
            val post = Post(
                id = "ai_post_${System.currentTimeMillis()}_$index",
                userId = "ai_${agent.second}",
                userName = agent.first,
                userProfileImage = "",
                content = content,
                imageUrl = "",
                likesCount = Random.nextInt(10, 500),
                commentsCount = Random.nextInt(1, 50),
                sharesCount = Random.nextInt(1, 30),
                isLiked = false,
                isBookmarked = false,
                visibility = PostVisibility.PUBLIC,
                timeAgo = listOf("1m ago", "5m ago", "15m ago", "30m ago", "1h ago", "2h ago", "3h ago")[Random.nextInt(7)]
            )

            repository.addAiPost(post)
        }
    }
}
