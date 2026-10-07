import React, { useState } from 'react';
import { Sparkles, CheckCircle2, XCircle, ArrowRight, FileText, BrainCircuit } from 'lucide-react';

const ATSResumeChecker = () => {
  const [candidateSkills, setCandidateSkills] = useState('Java, Spring Boot, SQL, REST APIs, Git, Docker');
  const [candidateExp, setCandidateExp] = useState(3.5);
  const [jobSkills, setJobSkills] = useState('Java, Spring Boot, Microservices, Kafka, AWS, Docker, Kubernetes');
  const [jobMinExp, setJobMinExp] = useState(3);
  const [scoreData, setScoreData] = useState(null);

  const handleCalculate = (e) => {
    e.preventDefault();

    const candList = candidateSkills.split(',').map(s => s.trim().toLowerCase()).filter(Boolean);
    const reqList = jobSkills.split(',').map(s => s.trim().toLowerCase()).filter(Boolean);

    const matched = reqList.filter(req => candList.some(c => c.includes(req) || req.includes(c)));
    const missing = reqList.filter(req => !candList.some(c => c.includes(req) || req.includes(c)));

    const skillScore = reqList.length > 0 ? (matched.length / reqList.length) * 100 : 100;
    
    // Exp score
    let expScore = 100;
    if (candidateExp < jobMinExp) {
      expScore = Math.max(0, (candidateExp / jobMinExp) * 100);
    }

    const overall = Math.round((skillScore * 0.7) + (expScore * 0.3));

    setScoreData({
      overallScore: overall,
      skillScore: Math.round(skillScore),
      expScore: Math.round(expScore),
      matchedSkills: matched,
      missingSkills: missing,
    });
  };

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div className="bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 rounded-3xl p-8 text-white space-y-3 shadow-xl">
        <div className="inline-flex items-center space-x-2 bg-white/20 text-white px-3 py-1 rounded-full text-xs font-semibold">
          <BrainCircuit className="h-4 w-4" />
          <span>Interactive ATS Engine</span>
        </div>
        <h1 className="text-3xl font-extrabold tracking-tight">ATS Resume Match Simulator</h1>
        <p className="text-blue-100 text-sm max-w-2xl leading-relaxed">
          Test how applicant tracking algorithms rank your technical profile against any enterprise job opening.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Input Form */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
          <h2 className="font-bold text-slate-900 text-base flex items-center space-x-2">
            <FileText className="h-5 w-5 text-blue-600" />
            <span>Profile & Job Specs</span>
          </h2>

          <form onSubmit={handleCalculate} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                Your Skills (comma-separated)
              </label>
              <textarea
                rows="3"
                value={candidateSkills}
                onChange={(e) => setCandidateSkills(e.target.value)}
                className="w-full p-3 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                Your Years of Experience
              </label>
              <input
                type="number"
                step="0.5"
                value={candidateExp}
                onChange={(e) => setCandidateExp(Number(e.target.value))}
                className="w-full p-2.5 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
              />
            </div>

            <div className="pt-2 border-t border-slate-100">
              <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                Target Job Required Skills
              </label>
              <textarea
                rows="3"
                value={jobSkills}
                onChange={(e) => setJobSkills(e.target.value)}
                className="w-full p-3 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                Minimum Experience Required (Years)
              </label>
              <input
                type="number"
                step="0.5"
                value={jobMinExp}
                onChange={(e) => setJobMinExp(Number(e.target.value))}
                className="w-full p-2.5 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
              />
            </div>

            <button
              type="submit"
              className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-xl shadow transition text-sm flex items-center justify-center space-x-2"
            >
              <Sparkles className="h-4 w-4" />
              <span>Simulate ATS Scoring</span>
            </button>
          </form>
        </div>

        {/* Results Card */}
        <div className="space-y-6">
          {scoreData ? (
            <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-6">
              <div className="text-center space-y-2 pb-6 border-b border-slate-100">
                <div className={`inline-flex items-center justify-center w-24 h-24 rounded-full text-3xl font-extrabold ${
                  scoreData.overallScore >= 80 ? 'bg-emerald-100 text-emerald-700' :
                  scoreData.overallScore >= 60 ? 'bg-blue-100 text-blue-700' : 'bg-amber-100 text-amber-700'
                }`}>
                  {scoreData.overallScore}%
                </div>
                <h3 className="font-bold text-slate-900 text-lg">Overall Match Probability</h3>
                <p className="text-xs text-slate-500">
                  {scoreData.overallScore >= 80 ? '🌟 Excellent! High chance of recruiter shortlist.' :
                   scoreData.overallScore >= 60 ? '👍 Good fit. Consider adding the missing skills below.' :
                   '⚠️ Profile needs enhancement to pass recruiter filters.'}
                </p>
              </div>

              {/* Matched skills */}
              <div>
                <h4 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-2 flex items-center space-x-1">
                  <CheckCircle2 className="h-4 w-4 text-emerald-600" />
                  <span>Matched Skills ({scoreData.matchedSkills.length})</span>
                </h4>
                <div className="flex flex-wrap gap-1.5">
                  {scoreData.matchedSkills.map((s, i) => (
                    <span key={i} className="px-2.5 py-1 bg-emerald-50 text-emerald-700 text-xs rounded-md font-medium border border-emerald-200">
                      {s}
                    </span>
                  ))}
                </div>
              </div>

              {/* Missing skills */}
              {scoreData.missingSkills.length > 0 && (
                <div>
                  <h4 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-2 flex items-center space-x-1">
                    <XCircle className="h-4 w-4 text-rose-600" />
                    <span>Skills to Add to Resume ({scoreData.missingSkills.length})</span>
                  </h4>
                  <div className="flex flex-wrap gap-1.5">
                    {scoreData.missingSkills.map((s, i) => (
                      <span key={i} className="px-2.5 py-1 bg-rose-50 text-rose-700 text-xs rounded-md font-medium border border-rose-200">
                        {s}
                      </span>
                    ))}
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="bg-slate-50 p-8 rounded-2xl border border-dashed border-slate-300 text-center space-y-3 h-full flex flex-col justify-center items-center">
              <Sparkles className="h-10 w-10 text-slate-400" />
              <h3 className="font-semibold text-slate-700 text-sm">Ready to analyze</h3>
              <p className="text-xs text-slate-500 max-w-xs">
                Fill out the skills above and hit "Simulate ATS Scoring" to see your instant ATS breakdown.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ATSResumeChecker;
