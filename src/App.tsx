import React, { useState, useEffect } from 'react';
import { 
  LayoutGrid, BookOpen, BarChart3, Settings, Bell, Star, 
  ChevronRight, PlayCircle, CheckCircle2, X, Clock, Trophy, Target, TrendingUp,
  RotateCcw, Award
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Radar, RadarChart, PolarGrid, PolarAngleAxis, ResponsiveContainer } from 'recharts';

// --- TYPES ---
interface Chapter {
  id: string;
  title: string;
  subject: string;
}

interface Question {
  text: string;
  options: string[];
  correct: number;
}

// --- DATASET ENHANCEMENT ---
const SYLLABUS: Chapter[] = [
  // Science
  { id: 'sci-1', title: 'Chemical Reactions and Equations', subject: 'Science' },
  { id: 'sci-2', title: 'Acids, Bases and Salts', subject: 'Science' },
  { id: 'sci-3', title: 'Metals and Non-metals', subject: 'Science' },
  { id: 'sci-4', title: 'Carbon and its Compounds', subject: 'Science' },
  { id: 'sci-5', title: 'Life Processes', subject: 'Science' },
  
  // Maths
  { id: 'math-1', title: 'Arithmetic Progressions', subject: 'Maths' },
  { id: 'math-2', title: 'Triangles', subject: 'Maths' },
  { id: 'math-3', title: 'Pair of Linear Equations', subject: 'Maths' },
  { id: 'math-4', title: 'Quadratic Equations', subject: 'Maths' },
  { id: 'math-5', title: 'Trigonometry', subject: 'Maths' },

  // Social Science
  { id: 'soc-1', title: 'Advent of Europeans to India', subject: 'Social Science' },
  { id: 'soc-2', title: 'The Extension of British Rule', subject: 'Social Science' },
  { id: 'soc-3', title: 'Impact of British Rule', subject: 'Social Science' },
  { id: 'soc-4', title: 'Indian Challenge to British Rule', subject: 'Social Science' },
  { id: 'soc-5', title: 'Freedom Movement', subject: 'Social Science' },
];

const QUESTIONS: Record<string, Question[]> = {
  'sci-1': [
    { text: "What is the chemical name of slaked lime?", options: ["Calcium carbonate", "Calcium oxide", "Calcium hydroxide", "Calcium chloride"], correct: 2 },
    { text: "Which gas is evolved when magnesium reacts with dilute HCl?", options: ["Oxygen", "Hydrogen", "Carbon dioxide", "Nitrogen"], correct: 1 },
    { text: "Rusting of iron is an example of:", options: ["Oxidation", "Reduction", "Decomposition", "None"], correct: 0 },
  ],
  'default': [
    { text: "What is the primary goal of this chapter?", options: ["Mastering concepts", "Skipping parts", "Only exams", "None"], correct: 0 },
    { text: "Success in SSLC depends on:", options: ["Luck", "Consistency", "Only textbooks", "Guessing"], correct: 1 },
  ]
};

export default function App() {
  const [showSplash, setShowSplash] = useState(true);
  const [tab, setTab] = useState('home');
  const [activeQuiz, setActiveQuiz] = useState<Chapter | null>(null);
  
  const [completed, setCompleted] = useState<string[]>(() => {
    const saved = localStorage.getItem('ad_completed');
    return saved ? JSON.parse(saved) : [];
  });
  
  const [scores, setScores] = useState<Record<string, number>>(() => {
    const saved = localStorage.getItem('ad_scores');
    return saved ? JSON.parse(saved) : {};
  });

  const [streak, setStreak] = useState(() => {
    const saved = localStorage.getItem('ad_streak');
    return saved ? parseInt(saved) : 0;
  });

  const [userName, setUserName] = useState(() => {
    return localStorage.getItem('ad_username') || 'Student User';
  });

  useEffect(() => {
    localStorage.setItem('ad_completed', JSON.stringify(completed));
    localStorage.setItem('ad_scores', JSON.stringify(scores));
    localStorage.setItem('ad_streak', streak.toString());
    localStorage.setItem('ad_username', userName);
  }, [completed, scores, streak, userName]);

  useEffect(() => {
    const timer = setTimeout(() => setShowSplash(false), 2500);
    return () => clearTimeout(timer);
  }, []);

  const handleReset = () => {
    if (window.confirm("Are you sure you want to clear all your progress? This cannot be undone.")) {
      setCompleted([]);
      setScores({});
      setStreak(0);
      localStorage.clear();
      setTab('home');
    }
  };

  const handleQuizComplete = (score: number) => {
    if (activeQuiz) {
      setScores(prev => ({...prev, [activeQuiz.id]: score}));
      if (score >= 40) setStreak(prev => prev + 1);
      setActiveQuiz(null);
    }
  };

  if (showSplash) {
    return (
      <div className="min-h-screen bg-[#0F172A] flex items-center justify-center">
        <motion.div 
          initial={{ opacity: 0, scale: 0.8 }} 
          animate={{ opacity: 1, scale: 1 }} 
          exit={{ opacity: 0, scale: 1.2 }}
          className="text-center"
        >
          <div className="w-32 h-32 bg-gradient-to-br from-indigo-500 to-blue-600 rounded-[2.5rem] flex items-center justify-center shadow-2xl mx-auto mb-6">
             <BookOpen size={64} className="text-white" />
          </div>
          <h1 className="text-4xl font-black text-white font-display tracking-tight">AksharaDeepa</h1>
          <p className="text-blue-400 font-bold mt-2 uppercase tracking-[0.3em] text-xs">Light of Knowledge</p>
          <div className="mt-12 flex justify-center gap-1.5">
             {[0,1,2].map(i => (
               <motion.div 
                 key={i}
                 animate={{ scale: [1, 1.5, 1], opacity: [0.3, 1, 0.3] }}
                 transition={{ repeat: Infinity, duration: 1, delay: i * 0.2 }}
                 className="w-2 h-2 bg-indigo-500 rounded-full"
               />
             ))}
          </div>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#0F172A] flex items-center justify-center p-4 font-sans antialiased">
      <div className="w-full max-w-[400px] h-[820px] bg-white rounded-[3.5rem] shadow-[0_0_100px_rgba(0,0,0,0.5)] relative overflow-hidden border-[12px] border-[#1E293B] flex flex-col">
        
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-36 h-7 bg-[#1E293B] rounded-b-3xl z-[60] flex items-center justify-center">
            <div className="w-10 h-1 bg-white/10 rounded-full" />
        </div>
        
        <div className="h-14 bg-white flex items-center justify-between px-8 pt-4 select-none z-50">
          <span className="text-sm font-bold text-slate-800">10:00</span>
          <div className="flex gap-1.5 items-center">
            <div className="flex gap-0.5">
                {[1,2,3,4].map(i => <div key={i} className="w-0.5 h-3 bg-slate-800 rounded-full" />)}
            </div>
            <div className="w-6 h-3 rounded-[3px] border border-slate-400 p-[1px]">
              <div className="w-full h-full bg-slate-800 rounded-[1px]" />
            </div>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto overflow-x-hidden scroll-smooth pb-24">
          <AnimatePresence mode="wait">
            {tab === 'home' && <HomeView key="home" completedCount={completed.length} streak={streak} userName={userName} />}
            {tab === 'syllabus' && (
              <SyllabusView 
                key="syllabus" 
                completed={completed} 
                onToggle={(id: string) => setCompleted(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id])} 
                onQuiz={(ch: Chapter) => setActiveQuiz(ch)} 
              />
            )}
            {tab === 'stats' && <StatsView key="stats" scores={scores} />}
            {tab === 'settings' && <SettingsView key="settings" onReset={handleReset} userName={userName} setUserName={setUserName} />}
          </AnimatePresence>
        </div>

        <nav className="absolute bottom-0 left-0 right-0 h-24 bg-white/80 backdrop-blur-xl border-t border-slate-100 flex items-center justify-around px-6 z-50">
          <NavBtn active={tab === 'home'} onClick={() => setTab('home')} icon={<LayoutGrid />} label="Home" />
          <NavBtn active={tab === 'syllabus'} onClick={() => setTab('syllabus')} icon={<BookOpen />} label="Subjects" />
          <NavBtn active={tab === 'stats'} onClick={() => setTab('stats')} icon={<BarChart3 />} label="Stats" />
          <NavBtn active={tab === 'settings'} onClick={() => setTab('settings')} icon={<Settings />} label="Profile" />
        </nav>

        <AnimatePresence>
          {activeQuiz && (
            <QuizOverlay 
              chapter={activeQuiz} 
              onClose={() => setActiveQuiz(null)} 
              onComplete={handleQuizComplete} 
            />
          )}
        </AnimatePresence>
      </div>

      <div className="hidden xl:flex flex-col ml-12 max-w-sm text-white/90">
        <h1 className="text-4xl font-extrabold font-display bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-indigo-400">AksharaDeepa</h1>
        <p className="mt-4 text-slate-400 leading-relaxed font-medium">SSLC Education Companion optimized for rural learning environments.</p>
        
        <div className="mt-8 space-y-4">
             <FeatureItem title="Smart Syllabus" desc="Tracking progress across 15+ SSLC chapters." color="blue" />
             <FeatureItem title="Offline First" desc="100% functional without internet connectivity." color="emerald" />
             <FeatureItem title="Analytics" desc="Identifying learning gaps using Radar Charts." color="orange" />
        </div>
      </div>
    </div>
  );
}

// --- SUB-COMPONENTS ---

function HomeView({ completedCount, streak, userName }: { completedCount: number, streak: number, userName: string }) {
  return (
    <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="p-6 space-y-6">
      <div className="flex items-center gap-3 mb-2">
        <div className="w-10 h-10 bg-indigo-600 rounded-xl flex items-center justify-center shadow-lg shadow-indigo-100">
           <BookOpen size={20} className="text-white" />
        </div>
        <h1 className="text-xl font-black text-slate-900 tracking-tight">AksharaDeepa</h1>
      </div>

      <div className="flex justify-between items-start">
        <div>
          <h2 className="text-2xl font-bold font-display tracking-tight text-slate-800">Namaste, {userName.split(' ')[0]}!</h2>
          <p className="text-slate-500 font-medium text-sm mt-0.5">Ready for your mission?</p>
        </div>
        <button className="w-12 h-12 bg-slate-50 rounded-2xl flex items-center justify-center text-slate-400 border border-slate-100">
           <Bell size={22} />
        </button>
      </div>

      <div className="bg-gradient-to-br from-indigo-600 to-blue-700 rounded-[2rem] p-7 text-white shadow-2xl shadow-blue-200 relative overflow-hidden group">
        <div className="absolute -right-4 -top-4 w-28 h-28 bg-white/10 rounded-full blur-2xl group-hover:scale-125 transition-transform" />
        <div className="flex justify-between items-start relative z-10">
          <div>
            <p className="text-indigo-100 text-[10px] font-black uppercase tracking-[0.2em] mb-1">Weekly Mission</p>
            <h3 className="text-2xl font-bold">Complete 1 Chapter</h3>
          </div>
          <div className="p-3 bg-white/20 rounded-2xl backdrop-blur-md">
            <Star size={22} fill="white" />
          </div>
        </div>
        
        <div className="mt-8 space-y-3 relative z-10">
          <div className="flex justify-between text-xs font-bold">
            <span className="opacity-80">Progress Tracking</span>
            <span>{Math.round((completedCount / SYLLABUS.length) * 100)}%</span>
          </div>
          <div className="h-2.5 bg-black/10 rounded-full overflow-hidden">
            <motion.div 
               initial={{ width: 0 }} 
               animate={{ width: `${(completedCount / SYLLABUS.length) * 100}%` }} 
               className="h-full bg-white rounded-full" 
            />
          </div>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
          <StatBox icon={<Award className="text-amber-500" />} label="Streak" value={`${streak} Days`} />
          <StatBox icon={<Target className="text-rose-500" />} label="Accuracy" value="88%" />
      </div>

      <div className="bg-slate-50 border border-slate-100 p-5 rounded-[1.5rem]">
         <div className="flex gap-3 items-center mb-2">
            <TrendingUp size={18} className="text-indigo-500" />
            <h4 className="font-bold text-sm text-slate-800">Quick Tip</h4>
         </div>
         <p className="text-xs text-slate-500 leading-relaxed font-medium">
            Master "Arithmetic Progressions" today to increase your total Maths score by 15%!
         </p>
      </div>
    </motion.div>
  );
}

function SyllabusView({ completed, onToggle, onQuiz }: { completed: string[], onToggle: (id: string) => void, onQuiz: (ch: Chapter) => void }) {
  const subjects = ["Science", "Maths", "Social Science"];
  const [selectedSub, setSelectedSub] = useState("Science");

  return (
    <motion.div initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} className="p-6 space-y-6">
      <h2 className="text-2xl font-bold font-display text-slate-900 mb-4">Study Tracker</h2>
      
      <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-none">
         {subjects.map(s => (
           <button 
             key={s} 
             onClick={() => setSelectedSub(s)}
             className={`px-5 py-2.5 rounded-full text-xs font-bold whitespace-nowrap transition-all ${selectedSub === s ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-100' : 'bg-slate-100 text-slate-500 hover:bg-slate-200'}`}
           >
             {s}
           </button>
         ))}
      </div>

      <div className="space-y-3">
        {SYLLABUS.filter(ch => ch.subject === selectedSub).map((ch, idx) => {
          const isDone = completed.includes(ch.id);
          return (
            <motion.div 
               layout
               key={ch.id} 
               className={`flex items-center gap-4 p-4 rounded-2xl transition-all border ${isDone ? 'bg-white border-slate-100' : 'bg-slate-50 border-transparent shadow-sm'}`}
            >
              <button 
                onClick={() => onToggle(ch.id)} 
                className={`transition-all ${isDone ? "text-emerald-500 scale-110" : "text-slate-300 hover:text-slate-400"}`}
              >
                <CheckCircle2 size={24} fill={isDone ? "currentColor" : "none"} />
              </button>
              <div className="flex-1">
                <p className={`text-sm font-bold leading-tight ${isDone ? 'line-through text-slate-400 font-medium' : 'text-slate-800'}`}>{ch.title}</p>
                <p className="text-[10px] font-bold text-slate-400 mt-0.5 uppercase tracking-wider">Mission {idx + 1}</p>
              </div>
              <button 
                onClick={() => onQuiz(ch)} 
                className="w-10 h-10 rounded-xl bg-white flex items-center justify-center text-indigo-600 shadow-sm border border-slate-100 hover:scale-105 active:scale-95 transition-transform"
              >
                <PlayCircle size={22} fill="white" />
              </button>
            </motion.div>
          );
        })}
      </div>
    </motion.div>
  );
}

function StatsView({ scores }: { scores: Record<string, number> }) {
  // Helper to calculate subject average
  const getSubjectAverage = (subject: string) => {
    const relevantChapters = SYLLABUS.filter(ch => ch.subject === subject || (subject === 'Social' && ch.subject === 'Social Science'));
    const subjectScores = relevantChapters
      .map(ch => scores[ch.id])
      .filter(s => s !== undefined) as number[];
    
    if (subjectScores.length === 0) return 0;
    return Math.round(subjectScores.reduce((a, b) => a + b, 0) / subjectScores.length);
  };

  const scienceScore = getSubjectAverage('Science');
  const mathsScore = getSubjectAverage('Maths');
  const socialScore = getSubjectAverage('Social Science');

  const chartData = [
    { subject: 'Science', score: Math.max(scienceScore, 20) }, 
    { subject: 'Maths', score: Math.max(mathsScore, 20) },
    { subject: 'Social', score: Math.max(socialScore, 20) },
  ];

  return (
    <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} className="p-6 space-y-6">
      <h2 className="text-2xl font-bold font-display text-slate-900">Strength Map</h2>
      <div className="h-72 w-full bg-white rounded-[2rem] shadow-sm border border-slate-50 p-4">
        <ResponsiveContainer width="100%" height="100%">
          <RadarChart cx="50%" cy="50%" outerRadius="75%" data={chartData}>
            <PolarGrid stroke="#f1f5f9" />
            <PolarAngleAxis dataKey="subject" tick={{fontSize: 12, fill: '#64748b', fontWeight: '800'}} />
            <Radar name="Mastery" dataKey="score" stroke="#4f46e5" fill="#4f46e5" fillOpacity={0.4} />
          </RadarChart>
        </ResponsiveContainer>
      </div>

      <div className="space-y-4">
          <h3 className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] px-1">Subject Performance</h3>
          
          <SubjectProgressCard title="Science" percentage={scienceScore} color="blue" />
          <SubjectProgressCard title="Maths" percentage={mathsScore} color="indigo" />
          <SubjectProgressCard title="Social Science" percentage={socialScore} color="emerald" />
      </div>

      <div className="bg-slate-50 border border-slate-100 p-6 rounded-[2rem] flex items-center gap-5">
             <div className="w-14 h-14 bg-white rounded-2xl flex items-center justify-center shadow-slate-200 shadow-lg">
                <Target className="text-slate-600" size={28} />
             </div>
             <div>
                <p className="text-2xl font-black text-slate-900">{Object.keys(scores).length}</p>
                <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">Chapters Mastered</p>
             </div>
      </div>
    </motion.div>
  );
}

function SubjectProgressCard({ title, percentage, color }: { title: string, percentage: number, color: string }) {
    const colors: any = { blue: "bg-blue-600", indigo: "bg-indigo-600", emerald: "bg-emerald-600" };
    const bgColors: any = { blue: "bg-blue-50", indigo: "bg-indigo-50", emerald: "bg-emerald-50" };

    return (
        <div className={`${bgColors[color]} p-5 rounded-[1.5rem] border border-white`}>
            <div className="flex justify-between items-center mb-3">
                <span className="font-bold text-slate-800 text-sm">{title}</span>
                <span className="text-xs font-black text-indigo-600">{percentage}%</span>
            </div>
            <div className="h-2 bg-white/50 rounded-full overflow-hidden">
                <motion.div 
                    initial={{ width: 0 }} 
                    animate={{ width: `${Math.max(percentage, 5)}%` }} 
                    className={`h-full ${colors[color]} rounded-full`}
                />
            </div>
        </div>
    );
}

function SettingsView({ onReset, userName, setUserName }: { onReset: () => void, userName: string, setUserName: (name: string) => void }) {
    return (
        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="p-6 space-y-8">
            <h2 className="text-2xl font-bold font-display text-slate-900">Profile Settings</h2>
            
            <div className="flex items-center gap-5 mb-8 p-4 bg-slate-50 rounded-[2rem]">
                <div className="w-16 h-16 bg-white rounded-2xl border-4 border-white shadow-xl flex items-center justify-center text-indigo-600 font-black text-2xl">{userName.charAt(0).toUpperCase()}</div>
                <div>
                   <h3 className="font-bold text-lg text-slate-800">{userName}</h3>
                   <p className="text-xs font-bold text-slate-400 uppercase">Grade 10 • SSLC</p>
                </div>
            </div>

            <div className="space-y-4">
                <div className="space-y-2 px-1">
                    <label className="text-[10px] font-black text-slate-400 uppercase tracking-widest pl-1">Personal Name</label>
                    <input 
                      type="text" 
                      value={userName} 
                      onChange={(e) => setUserName(e.target.value)}
                      className="w-full p-4 bg-white border border-slate-100 rounded-2xl font-medium text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 transition-all shadow-sm"
                      placeholder="Enter your name"
                    />
                </div>
            </div>

            <div className="pt-6 border-t border-slate-100">
                <button 
                  onClick={onReset}
                  className="w-full flex items-center justify-center gap-3 p-5 bg-rose-50 text-rose-600 rounded-2xl font-bold hover:bg-rose-100 transition-all active:scale-[0.98]"
                >
                    <RotateCcw size={20} />
                    <span>Reset All Progress</span>
                </button>
            </div>
        </motion.div>
    );
}

function QuizOverlay({ chapter, onClose, onComplete }: { chapter: Chapter, onClose: () => void, onComplete: (score: number) => void }) {
  const [idx, setIdx] = useState(0);
  const [selected, setSelected] = useState<number | null>(null);
  const quizSet = QUESTIONS[chapter.id] || QUESTIONS.default;
  const q = quizSet[idx] || quizSet[0];

  const handleNext = () => {
    if (idx < quizSet.length - 1) {
      setIdx(idx + 1);
      setSelected(null);
    } else {
      onComplete(100);
    }
  };

  return (
    <motion.div 
      initial={{ y: '100%' }} animate={{ y: 0 }} exit={{ y: '100%' }} transition={{ type: 'spring', damping: 25, stiffness: 200 }}
      className="absolute inset-0 bg-white z-[100] flex flex-col"
    >
      <div className="p-8 flex justify-between items-center bg-white pt-16">
        <button onClick={onClose} className="p-2 -ml-2 text-slate-400"><X size={26} /></button>
        <div className="flex items-center gap-2 text-indigo-600 font-bold bg-indigo-50 px-4 py-1.5 rounded-full text-sm">
           <Clock size={16} /> 02:00
        </div>
        <div className="w-10 h-10 rounded-2xl bg-slate-50 border border-slate-100 flex items-center justify-center text-xs font-black text-slate-500">
           {idx + 1}/{quizSet.length}
        </div>
      </div>

      <div className="flex-1 p-8 flex flex-col justify-center max-w-sm mx-auto w-full">
        <h3 className="text-2xl font-bold text-slate-900 leading-snug mb-10">{q.text}</h3>
        <div className="space-y-3">
          {q.options.map((opt, i) => (
            <button 
              key={i} 
              onClick={() => setSelected(i)} 
              className={`w-full p-5 rounded-2xl border-2 transition-all text-left font-bold text-sm ${selected === i ? 'border-indigo-600 bg-indigo-50 text-indigo-700 shadow-lg shadow-indigo-100' : 'border-slate-100 text-slate-600 hover:border-slate-200 bg-white'}`}
            >
              {opt}
            </button>
          ))}
        </div>
      </div>

      <div className="p-8 pb-12">
        <button disabled={selected === null} onClick={handleNext} className="w-full bg-indigo-600 disabled:bg-slate-200 disabled:text-slate-400 text-white py-5 rounded-3xl font-black text-lg shadow-xl shadow-indigo-200">
          {idx === quizSet.length - 1 ? 'Finish Mission' : 'Check & Next'}
        </button>
      </div>
    </motion.div>
  );
}

function NavBtn({ active, onClick, icon, label }: { active: boolean, onClick: () => void, icon: React.ReactElement, label: string }) {
  return (
    <button onClick={onClick} className={`flex flex-col items-center gap-1.5 px-3 py-2 rounded-2xl transition-all ${active ? 'text-indigo-600 bg-indigo-50/50' : 'text-slate-400'}`}>
      {React.cloneElement(icon, { size: 22, strokeWidth: active ? 3 : 2 })}
      <span className="text-[9px] font-black uppercase tracking-tighter">{label}</span>
    </button>
  );
}

function StatBox({ icon, label, value }: { icon: React.ReactNode, label: string, value: string }) {
    return (
        <div className="bg-white border border-slate-100 p-4 rounded-3xl shadow-sm flex flex-col items-center text-center">
            <div className="mb-2 p-2 bg-slate-50 rounded-xl">{icon}</div>
            <p className="text-lg font-black text-slate-800 leading-none">{value}</p>
            <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mt-1">{label}</p>
        </div>
    );
}

function FeatureItem({ title, desc, color }: { title: string, desc: string, color: 'blue' | 'emerald' | 'orange' }) {
    const colors = { blue: "bg-blue-400", emerald: "bg-emerald-400", orange: "bg-orange-400" };
    return (
        <div className="flex gap-4">
            <div className={`w-1 h-12 rounded-full ${colors[color]}`} />
            <div>
                <h4 className="text-sm font-bold text-white">{title}</h4>
                <p className="text-xs text-slate-500 font-medium">{desc}</p>
            </div>
        </div>
    );
}


