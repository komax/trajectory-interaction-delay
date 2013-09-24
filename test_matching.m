clear('java');
load synthetic_data/example_trajs2.mat; % t1 t2

%%%short1 = t1(7:9,:);
%%%short2 = t2(4:end-1,:);
%%%m_short = matching.discrete_lcfm(short1, short2);

%%%figure; visualise.plot_matching(short1, short2, m_short);

m = matching.discrete_lcfm(t1, t2);
figure; visualise.plot_matching(t1, t2, m);
figure; visualise.plot_path(t1, t2, m, 't1', 't2');

